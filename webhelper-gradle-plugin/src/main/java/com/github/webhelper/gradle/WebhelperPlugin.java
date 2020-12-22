package com.github.webhelper.gradle;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;

import java.util.List;
import java.util.Set;
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

        WebpackTask taskWebpack = project.getTasks().create("webpack", WebpackTask.class);
        taskWebpack.setExtension(ext);

        GenerateTask taskGenerate = project.getTasks().create("generateTs", GenerateTask.class);
        taskWebpack.dependsOn(taskGenerate);

        project.afterEvaluate(prj -> {
            System.out.println("after evaluation");

            List<Task> webpackDeps = project.getTasks()
                .stream().filter(t -> t.getName().startsWith("compile")
                    && !t.getName().contains("Test"))
                .collect(Collectors.toList());
            System.out.println("deps=" + webpackDeps.stream().map(Task::getName).collect(Collectors.joining(",")));
            taskGenerate.setDependsOn(webpackDeps);

            Task taskResources = prj.getTasks().findByName("processResources");
            System.out.println("processResources = " + taskResources);
            if (taskResources != null) {
               taskResources.dependsOn(taskWebpack);
            } else {
                project.getLogger().warn("task processResources not found.");
            }
        });
    }
}
