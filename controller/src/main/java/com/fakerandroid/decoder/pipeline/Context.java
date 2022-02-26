package com.fakerandroid.decoder.pipeline;

import com.fakerandroid.decoder.api.AndroidProject;
import com.fakerandroid.decoder.api.Apk;

import java.util.List;
import java.util.logging.Logger;

public class Context {

    TransformManager transformManager;

    AndroidProject androidProject;

    List<Apk> apks;

    public List<Apk> getApks() {
        return apks;
    }

    Logger logger = Logger.getLogger(this.getClass().getName());

    public TransformManager getTransformManager() {
        return transformManager;
    }

    public AndroidProject getAndroidProject() {
        return androidProject;
    }

    public Logger getLogger() {
        return logger;
    }

    public Context(TransformManager transformManager, List<Apk> apks, AndroidProject androidProject) {
        this.transformManager = transformManager;
        this.apks = apks;
        this.androidProject = androidProject;
    }

}
