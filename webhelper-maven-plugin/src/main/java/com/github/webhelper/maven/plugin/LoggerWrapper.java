package com.github.webhelper.maven.plugin;

import com.github.webhelper.core.util.Logger;
import org.apache.maven.plugin.logging.Log;

public class LoggerWrapper implements Logger {
    private Log log;
    public LoggerWrapper(Log log) {
        this.log = log;
    }


    @Override
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    @Override
    public void debug(CharSequence msg) {
        log.debug(msg);
    }

    @Override
    public void debug(CharSequence msg, Throwable t) {
        log.debug(msg, t);
    }

    @Override
    public void debug(Throwable t) {
        log.debug(t);
    }

    @Override
    public boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    @Override
    public void info(CharSequence msg) {
        log.info(msg);
    }

    @Override
    public void info(CharSequence msg, Throwable t) {
        log.info(msg, t);
    }

    @Override
    public void info(Throwable t) {
        log.info(t);
    }

    @Override
    public boolean isWarnEnabled() {
        return log.isWarnEnabled();
    }

    @Override
    public void warn(CharSequence msg) {
        log.warn(msg);
    }

    @Override
    public void warn(CharSequence msg, Throwable t) {
        log.warn(msg, t);
    }

    @Override
    public void warn(Throwable t) {
        log.warn(t);
    }

    @Override
    public boolean isErrorEnabled() {
        return log.isErrorEnabled();
    }

    @Override
    public void error(CharSequence msg) {
        log.error(msg);
    }

    @Override
    public void error(CharSequence msg, Throwable t) {
        log.error(msg, t);
    }

    @Override
    public void error(Throwable t) {
        log.error(t);
    }
}
