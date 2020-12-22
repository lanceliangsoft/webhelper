package com.github.webhelper.gradle;

import com.github.webhelper.core.Webpack;
import com.github.webhelper.core.model.WebpackRequest;
import com.github.webhelper.core.util.LoggerHolder;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleScriptException;
import org.gradle.api.tasks.TaskAction;

import javax.inject.Inject;
import java.io.File;
import java.util.HashMap;

public class WebpackTask extends DefaultTask {
    private WebpackExtension ext;
    public void setExtension(WebpackExtension ext) {
        this.ext = ext;
    }

    @Inject
    public WebpackTask() {
    }

    @TaskAction
    public void build() {
        System.out.println("webpack...");

        LoggerHolder.setLogger(new LoggerWrapper(getLogger()));

        try {
            WebpackRequest request = new WebpackRequest();
            request.setProjectName(getProject().getName());
            request.setProjectVersion(String.valueOf(getProject().getVersion()));
            File baseDir = getProject().getRootDir();
            request.setAppType(ext.getAppType());
            request.setBaseDir(baseDir);
            request.setOutputDir(getProject().getBuildDir());
            request.setDistDir(new File(getProject().getBuildDir(), "resources/main/static/assets"));
            request.setWebappDir(new File(baseDir, ext.getWebappDirectory()));
            request.setOutputFilename(ext.getOutputFilename());
            request.setEntry(ext.getEntry());
            request.setDependencies(ext.getDependencies());
            request.setDevDependencies(ext.getDevDependencies());

            new Webpack().build(request);
        } catch (Exception ex) {
            getLogger().error(ex.getMessage(), ex);
            throw new GradleScriptException(ex.getMessage(), ex);
        }
    }
}
