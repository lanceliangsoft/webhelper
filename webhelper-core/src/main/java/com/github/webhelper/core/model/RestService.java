package com.github.webhelper.core.model;

import java.util.ArrayList;
import java.util.List;

public class RestService {
    private String name;
    private List<Endpoint> endpoints = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Endpoint> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(List<Endpoint> endpoints) {
        this.endpoints = endpoints;
    }
}
