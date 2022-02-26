package com.fakerandroid.decoder.exception;

public class FakerAndroidException extends Exception {
    public FakerAndroidException(Throwable cause) {
        super(cause);
    }

    public FakerAndroidException(String message, Throwable cause) {
        super(message, cause);
    }

    public FakerAndroidException(String message) {
        super(message);
    }

    public FakerAndroidException() {
    }
}
