package com.github.webhelper.core.model;

public class Parameter extends NamedVar {
    private boolean isBody;

    public boolean isBody() {
        return isBody;
    }

    public void setBody(boolean body) {
        isBody = body;
    }
}
