package com.github.webhelper.gradle;

import org.gradle.api.logging.Logger;

public class LoggerWrapper implements com.github.webhelper.core.util.Logger {
    private Logger log;
    public LoggerWrapper(Logger log) {
        this.log = log;
    }


    @Override
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    @Override
    public void debug(CharSequence msg) {
        log.debug(String.valueOf(msg));
    }

    @Override
    public void debug(CharSequence msg, Throwable t) {
        log.debug(String.valueOf(msg), t);
    }

    @Override
    public void debug(Throwable t) {
        log.debug(t.getMessage(), t);
    }

    @Override
    public boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    @Override
    public void info(CharSequence msg) {
        log.info(String.valueOf(msg));
    }

    @Override
    public void info(CharSequence msg, Throwable t) {
        log.info(String.valueOf(msg), t);
    }

    @Override
    public void info(Throwable t) {
        log.info(t.getMessage(), t);
    }

    @Override
    public boolean isWarnEnabled() {
        return log.isWarnEnabled();
    }

    @Override
    public void warn(CharSequence msg) {
        log.warn(String.valueOf(msg));
    }

    @Override
    public void warn(CharSequence msg, Throwable t) {
        log.warn(String.valueOf(msg), t);
    }

    @Override
    public void warn(Throwable t) {
        log.warn(t.getMessage(), t);
    }

    @Override
    public boolean isErrorEnabled() {
        return log.isErrorEnabled();
    }

    @Override
    public void error(CharSequence msg) {
        log.error(String.valueOf(msg));
    }

    @Override
    public void error(CharSequence msg, Throwable t) {
        log.error(String.valueOf(msg), t);
    }

    @Override
    public void error(Throwable t) {
        log.error(t.getMessage(), t);
    }
}
