package com.github.webhelper.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;

import java.util.stream.Collectors;

public class WebhelperPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        System.out.println("===========webhelper.gradle.plugin =========");
        System.out.println("project: " + project.getName());
        // project.getTasks().register("webpack", WebpackTask.class);
        WebpackExtension ext =
                project.getExtensions().create(
                    "webpack", WebpackExtension.class);
        System.out.println("extension=" + ext);

        Task taskWebpack = project.getTasks().create("webpack", WebpackTask.class, ext);

        taskWebpack.setDependsOn(project.getTasks()
            .stream().filter(t -> t.getName().startsWith("compile"))
            .collect(Collectors.toList()));

        /*
        Task taskResources = project.getTasks().getAt("processResources");
        System.out.println("processResources = " + taskResources);
        if (taskResources == null) {
            taskResources = project.getTasks().getAt("classes");
            System.out.println("classes = " + taskResources);
        }
        if (taskResources != null) {
           taskResources.dependsOn(taskWebpack);
        } else {
            project.getLogger().warn("task processResources not found.");
        }
        */
    }
}
