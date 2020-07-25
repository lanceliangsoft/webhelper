package com.github.webhelper.maven.plugin;

import com.github.webhelper.core.Webpack;
import com.github.webhelper.core.util.LoggerHolder;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Mojo(name = "webpack")
public class WebpackMojo extends AbstractMojo {
    @Parameter(property = "project.name", required = true)
    private String projectName;

    @Parameter(property = "project.version", required = true)
    private String projectVersion;

    @Parameter(property = "project.basedir", required = true)
    private File basedir;

    @Parameter(property = "project.build.directory", required = true)
    private File outputDirectory;

    @Parameter(property = "webpack.entry", required = true)
    private String entry;

    @Parameter(property = "webpack.appType", required = true)
    private String appType;

    @Parameter(property = "webpack.webappDirectory", required = true)
    private String webappDirectory;

    @Parameter(property = "webpack.distDirectory", defaultValue = "target/generatedResources/public/asset")
    private String distDirectory;

    @Parameter(property = "webpack.outputFilename", defaultValue = "bundle.js")
    private String outputFilename;

    public void execute() throws MojoExecutionException {
        LoggerHolder.setLogger(new LoggerWrapper(getLog()));

        try {
            Map<String, String> props = new HashMap<>();
            props.put("projectName", projectName);
            props.put("projectVersion", projectVersion);
            props.put("basedir", basedir.getAbsolutePath());
            props.put("outputDirectory", outputDirectory.getAbsolutePath());
            props.put("entry", entry);
            props.put("appType", appType);
            props.put("distDirectory", distDirectory);
            props.put("webappDirectory", webappDirectory);
            props.put("outputFilename", outputFilename);

            new Webpack().build(props);
        } catch (Exception ex) {
            getLog().error(ex.getMessage(), ex);
            throw new MojoExecutionException(ex.getMessage(), ex);
        }
    }
}
