package com.github.webhelper.core.model;

import java.util.ArrayList;
import java.util.List;

public class TsModel {
    private String tsType;
    private List<NamedVar> properties = new ArrayList<>();

    public String getTsType() {
        return tsType;
    }

    public void setTsType(String tsType) {
        this.tsType = tsType;
    }

    public List<NamedVar> getProperties() {
        return properties;
    }

    public void setProperties(List<NamedVar> properties) {
        this.properties = properties;
    }
}
