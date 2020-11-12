package com.github.webhelper.gradle;

import com.github.webhelper.core.Webpack;
import com.github.webhelper.core.util.LoggerHolder;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleScriptException;
import org.gradle.api.tasks.TaskAction;

import java.util.HashMap;
import java.util.Map;

public class WebpackTask extends DefaultTask {
    private WebpackExtension ext;

    public WebpackTask(WebpackExtension ext) {
        this.ext = ext;
    }

    @TaskAction
    public void build() {
        LoggerHolder.setLogger(new LoggerWrapper(getLogger()));

        try {
            Map<String, String> props = new HashMap<>();
            props.put("projectName", getProject().getName());
            props.put("projectVersion", String.valueOf(getProject().getVersion()));
            props.put("basedir", getProject().getRootDir().getAbsolutePath());
            props.put("relativeOutputDirectory", "build");
            props.put("outputDirectory", getProject().getBuildDir().getAbsolutePath());
            props.put("entry", ext.getEntry());
            props.put("appType", ext.getAppType());
            props.put("distDirectory", ext.getDistDirectory());
            props.put("webappDirectory", ext.getWebappDirectory());
            props.put("outputFilename", ext.getOutputFilename());

            new Webpack().build(props);
        } catch (Exception ex) {
            getLogger().error(ex.getMessage(), ex);
            throw new GradleScriptException(ex.getMessage(), ex);
        }
    }
}
