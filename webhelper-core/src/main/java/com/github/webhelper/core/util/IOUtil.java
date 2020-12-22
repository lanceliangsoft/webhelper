package com.github.webhelper.core.util;

import freemarker.cache.URLTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static freemarker.template.Configuration.VERSION_2_3_30;

public class IOUtil {
  public static void ensureDir(File dir) {
    if (!dir.exists()) {
      dir.mkdirs();
    }
  }

  public static String readResource(String resname) throws IOException {
    char[] buf = new char[4096];
    StringBuilder sb = new StringBuilder();
    InputStream inputStream = IOUtil.class.getResourceAsStream(resname);
    if (inputStream == null) {
      throw new IOException("resource " + resname + " not found.");
    }
    try (InputStreamReader in = new InputStreamReader(inputStream, Charset.defaultCharset())) {
      int len;
      while ((len = in.read(buf)) >= 0) {
        sb.append(buf, 0, len);
      }
    }
    return sb.toString();
  }

  public static void writeText(File file, String text) throws IOException {
    try (FileWriter out = new FileWriter(file)) {
      out.write(text);
    }
  }

  public static void generateFile(
      String templateFile, File outputFile, Map<String, Object> variables, boolean overwrite)
      throws IOException, TemplateException {
    if (outputFile.exists() && !overwrite) {
      return;
    }
    ensureDir(outputFile.getParentFile());

    if (templateFile.endsWith(".ftl")) {
      generateFreeMarkerFile(templateFile, outputFile, variables);
    } else {
      generatePlainFile(templateFile, outputFile, variables);
    }
  }

  static void generatePlainFile(String templateFile, File outputFile, Map<String, Object> variables)
      throws IOException {
    String text = readResource("/META-INF/templates/" + templateFile);
    String newText =
        replace(
            text, "@([\\w_]+)@", 1, varName -> String.valueOf(variables.getOrDefault(varName, "")));
    writeText(outputFile, newText);
  }

  static void generateFreeMarkerFile(
      String templateFile, File outputFile, Map<String, Object> variables)
      throws IOException, TemplateException {
   // String text = readResource("/META-INF/templates/" + templateFile);
    Configuration cfg = new Configuration(VERSION_2_3_30);
    cfg.setTemplateLoader(
        new URLTemplateLoader() {
          @Override
          protected URL getURL(String name) {
            URL url = IOUtil.class.getClassLoader().getResource("META-INF/templates/" + name);
            System.out.println("url=" + url);
            return url;
          }
        });

    cfg.setDefaultEncoding("UTF-8");
    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    cfg.setLogTemplateExceptions(false);
    cfg.setWrapUncheckedExceptions(true);
    cfg.setFallbackOnNullLoopVariable(false);

    Template template = cfg.getTemplate(templateFile);
    try (FileWriter out = new FileWriter(outputFile)) {
      template.process(variables, out);
    }
  }

  public static String replace(
      String text, String regex, int groupIndex, Function<String, String> replaceFunc) {
    StringBuffer sb = new StringBuffer();
    Pattern pattern = Pattern.compile(regex);
    Matcher m = pattern.matcher(text);
    while (m.find()) {
      String value = replaceFunc.apply(m.group(groupIndex));
      m.appendReplacement(sb, value);
    }
    m.appendTail(sb);
    return sb.toString();
  }

  public static String relativePath(File dir, File dirBase) {
    return dirBase.toPath().relativize(dir.toPath()).toString();
  }

  public static RuntimeException toRuntimeException(Exception ex) {
    return new IllegalArgumentException(ex.getMessage(), ex);
  }
}
