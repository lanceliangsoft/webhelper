package com.github.webhelper.maven.plugin;

import com.github.webhelper.core.GenerateTask;
import com.github.webhelper.core.util.LoggerHolder;
import com.google.inject.internal.util.Strings;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Mojo(name = "generate", requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class GenerateMojo extends AbstractMojo {
    @Parameter(property = "project")
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        LoggerHolder.setLogger(new LoggerWrapper(getLog()));
        try {
            File basedir = project.getBasedir();
            String outputDirectory = project.getBuild().getDirectory();
            String pathSep = System.getProperty("path.separator");

            Map<String, String> props = new HashMap<>();
            props.put("projectName", project.getName());
            props.put("projectVersion", project.getVersion());
            props.put("basedir", basedir.getAbsolutePath());
            props.put("compilePath", String.join(pathSep, project.getCompileClasspathElements()));
            props.put("outputDirectory", outputDirectory);
            props.put("relativeOutputDirectory", "target");

            new GenerateTask().generate(props);
        } catch (Exception ex) {
            getLog().error(ex.getMessage(), ex);
            throw new MojoExecutionException(ex.getMessage(), ex);
        }
    }
}
