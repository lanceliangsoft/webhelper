package com.github.webhelper.core;

import com.github.webhelper.core.model.AppMeta;
import com.github.webhelper.core.util.CmdExector;
import com.github.webhelper.core.util.LoggerHolder;
import com.google.gson.GsonBuilder;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.webhelper.core.util.DataUtil.addList;
import static com.github.webhelper.core.util.IOUtil.generateFile;
import static com.github.webhelper.core.util.IOUtil.readResource;

public class Webpack {
    public void build(Map<String, String> props) throws IOException, InterruptedException, TemplateException {
        String appType = props.get("appType");
        File basedir = new File(props.get("basedir"));
        File outputDirectory = new File(props.get("outputDirectory"));
        File distDirectory = new File(basedir, props.get("distDirectory"));
        File staticDir = distDirectory.getParentFile();
        LoggerHolder.getLogger().info("staticDir=" + staticDir);
        String assetsPath = distDirectory.getName();
        LoggerHolder.getLogger().info("assetsPath=" + assetsPath);
        props.put("assetsPath", assetsPath);

        String metaJson = readResource("/META-INF/templates/" + appType + "/meta.json");
        AppMeta appMeta = new GsonBuilder().create().fromJson(metaJson, AppMeta.class);

        Map<String, Object> vars = new HashMap<>(props);
        generateFile(
            appType + "/package.json",
            new File(basedir, "package.json"),
            vars,
            false);

        File webpackDir = new File(outputDirectory, "webpack");
        generateFile(
            appType + "/webpack.config.js",
            new File(webpackDir, "webpack.config.js"),
            vars,
            true);

        generateFile(
            appType + "/tsconfig.json",
            new File(webpackDir, "tsconfig.json"),
            vars,
            true);

        CmdExector cmdExector = new CmdExector();
        cmdExector.setDir(basedir);

        File nodeModulesDir = new File(basedir, "node_modules");
        List<String> missingDevDependencies = appMeta.devDependencies.stream()
            .filter(moduleName -> !new File(nodeModulesDir, moduleName + "/package.json").exists())
            .collect(Collectors.toList());

        if (!missingDevDependencies.isEmpty()) {
            cmdExector.exec(addList("npm", "install", "--save-dev",
                missingDevDependencies));
        }

        cmdExector.exec("npm", "run", "build");

        generateFile(
            appType + "/index.html",
            new File(staticDir, "index.html"),
            vars,
            true);
    }
}
