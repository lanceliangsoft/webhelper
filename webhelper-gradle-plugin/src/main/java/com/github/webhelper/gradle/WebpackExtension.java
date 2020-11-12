package com.github.webhelper.gradle;

public class WebpackExtension {
    private String entry;

    private String appType;

    private String webappDirectory;

    private String distDirectory = "target/generatedResources/public/asset";

    private String outputFilename = "bundle.js";

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getWebappDirectory() {
        return webappDirectory;
    }

    public void setWebappDirectory(String webappDirectory) {
        this.webappDirectory = webappDirectory;
    }

    public String getDistDirectory() {
        return distDirectory;
    }

    public void setDistDirectory(String distDirectory) {
        this.distDirectory = distDirectory;
    }

    public String getOutputFilename() {
        return outputFilename;
    }

    public void setOutputFilename(String outputFilename) {
        this.outputFilename = outputFilename;
    }
}
