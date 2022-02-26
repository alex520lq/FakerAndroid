package com.fakerandroid.decoder.exception;

public class DexToSmaliException extends Exception{
    public DexToSmaliException(Throwable cause) {
        super(cause);
    }

    public DexToSmaliException(String message, Throwable cause) {
        super(message, cause);
    }

    public DexToSmaliException(String message) {
        super(message);
    }

    public DexToSmaliException() {
    }
}
