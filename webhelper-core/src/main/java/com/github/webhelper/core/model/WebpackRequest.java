package com.github.webhelper.core.model;

import java.io.File;
import java.util.List;

public class WebpackRequest {
    private String projectName;
    private String projectVersion;
    private String appType;
    private File baseDir;
    private File outputDir;
    private File distDir;
    private File webappDir;
    private String entry;
    private String outputFilename;
    private List<String> dependencies;
    private List<String> devDependencies;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectVersion() {
        return projectVersion;
    }

    public void setProjectVersion(String projectVersion) {
        this.projectVersion = projectVersion;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public File getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(File baseDir) {
        this.baseDir = baseDir;
    }

    public File getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(File outputDir) {
        this.outputDir = outputDir;
    }

    public File getDistDir() {
        return distDir;
    }

    public void setDistDir(File distDir) {
        this.distDir = distDir;
    }

    public File getWebappDir() {
        return webappDir;
    }

    public void setWebappDir(File webappDir) {
        this.webappDir = webappDir;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public List<String> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<String> dependencies) {
        this.dependencies = dependencies;
    }

    public List<String> getDevDependencies() {
        return devDependencies;
    }

    public void setDevDependencies(List<String> devDependencies) {
        this.devDependencies = devDependencies;
    }

    public String getOutputFilename() {
        return outputFilename;
    }

    public void setOutputFilename(String outputFilename) {
        this.outputFilename = outputFilename;
    }
}
