package com.github.webhelper.example.javareact.service;

public class ApplicationException extends RuntimeException {
    public ApplicationException(String msg) {
        super(msg);
    }

    public ApplicationException(String msg, Throwable t) {
        super(msg, t);
    }
}
