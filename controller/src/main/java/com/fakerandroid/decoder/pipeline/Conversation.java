package com.fakerandroid.decoder.pipeline;

public abstract class Conversation {
    public Context context;
    public Conversation(Context context) {
        this.context = context;
    }
    public abstract void converse();
}
