package com.natpryce.piazza.pluginConfiguration;

public class SaveConfigFailedException extends RuntimeException {

    public SaveConfigFailedException(Throwable e) {
        super(e);
    }

    public SaveConfigFailedException(String message) {
        super(message);
    }
}
