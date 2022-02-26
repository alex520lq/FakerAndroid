package com.fakerandroid.decoder.exception;

public class FileException extends RuntimeException {
    private static final long serialVersionUID = -7410848445429898248L;

    public FileException(String message) {
        super(message);
    }

    public FileException(String message, Throwable cause) {
        super(message, cause);
    }
}
