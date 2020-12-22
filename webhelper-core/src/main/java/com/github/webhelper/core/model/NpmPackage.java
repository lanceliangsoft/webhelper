package com.github.webhelper.core.model;

import java.util.Map;

public class NpmPackage {
    public String name;
    public String version;
    public String description;
    public Map<String, String> dependencies;
    public Map<String, String> devDependencies;
}
