package com.github.webhelper.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class WebhelperPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        System.out.println("===========webhelper.gradle.plugin =========");
        System.out.println("project: " + project.getName());
        // project.getTasks().register("webpack", WebpackTask.class);

        /*
        WebhelperPluginExtension config =
                project.getExtensions().create("webhelper", WebhelperPluginExtension.class);

        Task taskWebpack = project.getTasks().create("webpack", task -> {
            System.out.println("configuring webpack ...");
        });

        taskWebpack.doLast(task -> {
            System.out.println("doing webpack message=" + config.getMessage() + "...");
        });
        // taskWebpack.setDependsOn(project.getTasksByName("compileJava", false));

        Task taskResources = project.getTasks().getByName("processResources");
        taskResources.getDependsOn().add(taskWebpack);
        */
    }
}
