package com.github.webhelper.core;

import com.github.webhelper.core.model.AppMeta;
import com.github.webhelper.core.model.NpmPackage;
import com.github.webhelper.core.model.WebpackRequest;
import com.github.webhelper.core.util.CmdExector;
import com.github.webhelper.core.util.IOUtil;
import com.github.webhelper.core.util.LoggerHolder;
import com.google.gson.GsonBuilder;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.webhelper.core.util.DataUtil.addList;
import static com.github.webhelper.core.util.IOUtil.generateFile;
import static com.github.webhelper.core.util.IOUtil.readResource;

public class Webpack {
    public void build(WebpackRequest request) throws IOException, InterruptedException, TemplateException {
        File staticDir = request.getDistDir().getParentFile();
        LoggerHolder.getLogger().info("staticDir=" + staticDir);

        String metaJson = readResource("/META-INF/templates/" + request.getAppType() + "/meta.json");
        AppMeta appMeta = new GsonBuilder().create().fromJson(metaJson, AppMeta.class);
        Set<String> dependencies = new HashSet<>(appMeta.dependencies);
        Set<String> devDependencies = new HashSet<>(appMeta.devDependencies);
        if (request.getDependencies() != null) {
            dependencies.addAll(request.getDependencies());
        }
        if (request.getDevDependencies() != null) {
            devDependencies.addAll(request.getDevDependencies());
        }

        Map<String, Object> vars = new HashMap<>();
        vars.put("projectName", request.getProjectName());
        vars.put("projectVersion", request.getProjectVersion());
        vars.put("appType", request.getAppType());
        vars.put("entry", request.getEntry());
        vars.put("basedir", request.getBaseDir().getAbsolutePath());
        vars.put("outputDirectory", IOUtil.relativePath(request.getOutputDir(), request.getBaseDir()));
        vars.put("relBuildDir", IOUtil.relativePath(request.getOutputDir(), request.getBaseDir()));
        vars.put("distDirectory", IOUtil.relativePath(request.getDistDir(), request.getBaseDir()));
        vars.put("webappDirectory", IOUtil.relativePath(request.getWebappDir(), request.getBaseDir()));
        vars.put("assetsPath", request.getDistDir().getName());
        vars.put("outputFilename", request.getOutputFilename());

        System.out.println("vars={" + vars.entrySet().stream()
            .map(e -> e.getKey() + ": \"" + e.getValue() + "\"")
            .collect(Collectors.joining("\n")) + "}");

        File filePackageJson = new File(request.getBaseDir(), "package.json");
        generateFile(
            request.getAppType() + "/package.json",
            filePackageJson,
            vars,
            false);

        NpmPackage npmPackage;
        try (FileReader reader = new FileReader(filePackageJson)) {
            npmPackage = new GsonBuilder().create().fromJson(reader, NpmPackage.class);
        }

        File webpackDir = new File(request.getOutputDir(), "webpack");
        generateFile(
            request.getAppType() + "/webpack.config.js",
            new File(webpackDir, "webpack.config.js"),
            vars,
            true);

        generateFile(
            request.getAppType() + "/tsconfig.json",
            new File(webpackDir, "tsconfig.json"),
            vars,
            true);

        CmdExector cmdExector = new CmdExector();
        cmdExector.setDir(request.getBaseDir());

        File nodeModulesDir = new File(request.getBaseDir(), "node_modules");
        List<String> missingDependencies = dependencies.stream()
            .filter(moduleName -> !new File(nodeModulesDir, moduleName + "/package.json").exists()
                || (!npmPackage.dependencies.containsKey(moduleName)
                && !npmPackage.devDependencies.containsKey(moduleName)))
            .collect(Collectors.toList());

        if (!missingDependencies.isEmpty()) {
            cmdExector.exec(addList("npm", "install", "--save",
                missingDependencies));
        }

        List<String> missingDevDependencies = devDependencies.stream()
            .filter(moduleName -> !new File(nodeModulesDir, moduleName + "/package.json").exists()
                || (!npmPackage.dependencies.containsKey(moduleName)
                && !npmPackage.devDependencies.containsKey(moduleName)))
            .collect(Collectors.toList());

        if (!missingDevDependencies.isEmpty()) {
            cmdExector.exec(addList("npm", "install", "--save-dev",
                missingDevDependencies));
        }

        cmdExector.exec("npm", "run", "build");

        generateFile(
            request.getAppType() + "/index.html",
            new File(staticDir, "index.html"),
            vars,
            true);
    }
}
