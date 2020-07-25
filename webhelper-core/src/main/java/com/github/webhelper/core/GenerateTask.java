package com.github.webhelper.core;

import com.github.webhelper.core.model.Endpoint;
import com.github.webhelper.core.model.NamedVar;
import com.github.webhelper.core.model.Parameter;
import com.github.webhelper.core.model.RestService;
import com.github.webhelper.core.model.TsModel;
import com.github.webhelper.core.util.DataUtil;
import com.github.webhelper.core.util.IOUtil;
import com.github.webhelper.core.util.Logger;
import com.github.webhelper.core.util.LoggerHolder;
import com.github.webhelper.core.util.ReflectUtil;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.webhelper.core.util.IOUtil.replace;
import static com.github.webhelper.core.util.IOUtil.toRuntimeException;
import static com.github.webhelper.core.util.ReflectUtil.invoke;

public class GenerateTask {
  private Logger logger;
  private URLClassLoader classLoader;
  private Class<? extends Annotation> clzRestController;
  private Class<? extends Annotation> clzRequestMapping;
  private Class<? extends Annotation> clzGetMapping;
  private Class<? extends Annotation> clzPostMapping;
  private Class<? extends Annotation> clzPutMapping;
  private Class<? extends Annotation> clzDeleteMapping;

  private List<RestService> services;
  private Map<String, TsModel> typeMappings = new HashMap<>();

  private static Map<String, String> basicTypesMap =
      DataUtil.asMap(
          "?", "any",
          "java.lang.String", "string",
          "int", "number",
          "long", "number",
          "float", "number",
          "double", "number",
          "java.lang.Integer", "number",
          "java.lang.Long", "number",
          "java.lang.Float", "number",
          "java.lang.Double","number",
          "java.util.Date", "Date"
      );

  @SuppressWarnings("unchecked")
  public void generate(Map<String, String> props)
      throws IOException, ClassNotFoundException, TemplateException {
    logger = LoggerHolder.getLogger();

    services = new ArrayList<>();
    String pathSep = System.getProperty("path.separator");
    logger.info("generating TS codes...");
    File outputDir = new File(props.get("outputDirectory"));
    File generateTsDir = new File(outputDir, "generatedTs");

    String classpath = props.get("compilePath");
    logger.debug("classpath=" + classpath);
    String[] classpathParts = classpath.split(pathSep);

    String classesDir =
        Stream.of(classpathParts)
            .filter(p -> Files.isDirectory(Paths.get(p)))
            .findFirst()
            .orElseThrow(() -> new IOException("No directories in classpath"));
    Path pathClasses = Paths.get(classesDir);

    System.out.println("classes=" + pathClasses);

    URL[] urls = Stream.of(classpathParts).map(GenerateTask::toURL).toArray(URL[]::new);
    classLoader = new URLClassLoader(urls);

    clzRestController =
        (Class<? extends Annotation>)
            classLoader.loadClass("org.springframework.web.bind.annotation.RestController");
    clzRequestMapping =
        (Class<? extends Annotation>)
            classLoader.loadClass("org.springframework.web.bind.annotation.RequestMapping");
    clzGetMapping =
        (Class<? extends Annotation>)
            classLoader.loadClass("org.springframework.web.bind.annotation.GetMapping");
    clzPostMapping =
        (Class<? extends Annotation>)
            classLoader.loadClass("org.springframework.web.bind.annotation.PostMapping");
    clzPutMapping =
        (Class<? extends Annotation>)
            classLoader.loadClass("org.springframework.web.bind.annotation.PutMapping");
    clzDeleteMapping =
        (Class<? extends Annotation>)
            classLoader.loadClass("org.springframework.web.bind.annotation.DeleteMapping");

    Files.find(
            pathClasses,
            100,
            (filePath, fileAttrs) ->
                fileAttrs.isRegularFile() && filePath.getFileName().toString().endsWith(".class"))
        .forEach(p -> doClassFile(pathClasses, p, classLoader));

    generateFiles(generateTsDir);
  }

  void doClassFile(Path pathClasses, Path pathClass, ClassLoader classLoader) {
    String className =
        pathClasses
            .relativize(pathClass)
            .toString()
            .replaceFirst("\\.class$", "")
            .replace("/", ".");

    // logger.info("loading " + className + " (" + path.relativize(p).toString() + ")");
    try {
      Class<?> clazz = classLoader.loadClass(className);

      if (clazz.isAnnotationPresent(clzRestController)) {
        logger.info("controller class=" + clazz);

        RestService restService = new RestService();
        String serviceName = className.replaceFirst("^.*\\.", "").replace("Controller", "Service");
        restService.setName(serviceName);

        String basePath = getMappingPath(clazz);

        for (Method method : clazz.getMethods()) {
          Endpoint endpoint = new Endpoint();
          endpoint.setName(method.getName());

          Annotation mapping =
              DataUtil.eitherOf(
                  method.getAnnotation(clzRequestMapping),
                  method.getAnnotation(clzGetMapping),
                  method.getAnnotation(clzPostMapping),
                  method.getAnnotation(clzPutMapping),
                  method.getAnnotation(clzDeleteMapping));
          if (mapping != null) {
            String path = DataUtil.eitherOf(invoke(mapping, "value"), invoke(mapping, "path"));
            endpoint.setMethod(getMethod(mapping));

            Set<String> vars = new HashSet<>();
            String url = joinPath(basePath, path, vars);
            endpoint.setUrl(url);

            endpoint
                .getParams()
                .addAll(
                    vars.stream()
                        .map(
                            v -> {
                              Parameter param = new Parameter();
                              param.setName(v);
                              param.setType("string");
                              return param;
                            })
                        .collect(Collectors.toList()));

            endpoint.setReturnType(mapType(method.getGenericReturnType()));

            logger.info(
                "@GetMapping path="
                    + path
                    + " method="
                    + method.getName()
                    + ", return="
                    + endpoint.getUrl());

            restService.getEndpoints().add(endpoint);
          }
        }

        services.add(restService);
      }
    } catch (Exception ex) {
      logger.error("fail to load " + className, ex);
    }
  }

  String getMethod(Annotation mapping) {
    if (clzGetMapping.isInstance(mapping)) {
      return "GET";
    } else if (clzPostMapping.isInstance(mapping)) {
      return "POST";
    } else if (clzPutMapping.isInstance(mapping)) {
      return "PUT";
    } else if (clzDeleteMapping.isInstance(mapping)) {
      return "DELETE";
    } else if (clzRequestMapping.isInstance(mapping)) {
      try {
        return ((Object[]) ReflectUtil.invoke(mapping, "method"))[0].toString();
      } catch (Exception e) {
        throw toRuntimeException(e);
      }
    } else {
      throw new IllegalArgumentException("Unknown annotation " + mapping);
    }
  }

  String joinPath(String basePath, String path, Collection<String> vars) {
    StringBuilder sb = new StringBuilder();
    sb.append("\"");
    if (basePath != null && basePath.length() > 0 && !"/".equals(basePath)) {
      sb.append(basePath);
    }
    if (!path.startsWith("/")) {
      sb.append("/");
    }
    sb.append(
        replace(
            path,
            "\\{([\\w_]+)\\}",
            1,
            var -> {
              vars.add(var);
              return "\" + encodeURIComponent(" + var + ") + \"";
            }));
    sb.append("\"");
    return sb.toString().replaceFirst(" \\+ \"\"$", "");
  }

  private String mapType(Type javaType) {
    String typeName = javaType.getTypeName();

    if (javaType instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) javaType;
      String tsType = "any";
      boolean isArray = Iterable.class.isAssignableFrom((Class<?>) parameterizedType.getRawType());

      List<String> tsParamTypes =
          Stream.of(parameterizedType.getActualTypeArguments())
              .map(this::mapType)
              .collect(Collectors.toList());
      if (isArray) {
        tsType = tsParamTypes.get(0) + "[]";
      }
      return tsType;
    } else {
      if (basicTypesMap.containsKey(typeName)) {
        return basicTypesMap.get(typeName);
      }

      return convertModelClass(typeName).getTsType();

    }
  }

  private String getMappingPath(AnnotatedElement annotatedElement)
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    Annotation annotation =
        DataUtil.eitherOf(
            annotatedElement.getAnnotation(clzRequestMapping),
            annotatedElement.getAnnotation(clzGetMapping),
            annotatedElement.getAnnotation(clzPostMapping));

    if (annotation != null) {
      String path = DataUtil.eitherOf(invoke(annotation, "value"), invoke(annotation, "path"));
      if (path != null) {
        return path;
      }
    }
    return "";
  }

  private TsModel convertModelClass(String typeName) {
    if (typeMappings.containsKey(typeName)) {
      return typeMappings.get(typeName);
    }

    String tsType = typeName.replaceFirst("^.*\\.", "");
    try {
      Class<?> clazz = classLoader.loadClass(typeName);
      TsModel typeMapping = new TsModel();
      typeMapping.setTsType(tsType);
      ReflectUtil.getProperties(clazz).forEach((propName, propType) -> {
        String propTsType = mapType(propType);
        typeMapping.getProperties().add(new NamedVar(propName, propTsType));
      });
      typeMappings.put(typeName, typeMapping);
      return typeMapping;
    } catch (ClassNotFoundException ex) {
      logger.error(ex.getMessage(), ex);
      throw new IllegalArgumentException("not found " + typeName + " " + ex.getMessage());
    }
  }

  private void generateFiles(File generateTsDir) throws IOException, TemplateException {
    Map<String, Object> vars = DataUtil.asMap(
        "services", services,
        "models", typeMappings.values());

    IOUtil.generateFile(
        "rest/Service.ts.ftl", new File(generateTsDir, "service/RestService.ts"), vars, true);
  }

  private static URL toURL(String pathname) {
    try {
      return new File(pathname).toURI().toURL();
    } catch (MalformedURLException ex) {
      throw new IllegalArgumentException(ex.getMessage(), ex);
    }
  }
}
