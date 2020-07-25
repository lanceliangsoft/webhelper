package com.github.webhelper.core.util;

public class LoggerHolder {
    private static Logger logger;

    public static void setLogger(Logger logger) {
        LoggerHolder.logger = logger;
    }

    public static Logger getLogger() {
        return logger;
    }
}
