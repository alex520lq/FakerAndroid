package com.fakerandroid.decoder.smali;

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
