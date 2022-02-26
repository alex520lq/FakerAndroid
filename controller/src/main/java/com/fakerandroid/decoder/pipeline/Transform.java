package com.fakerandroid.decoder.pipeline;

import com.fakerandroid.decoder.api.AndroidProject;
import com.fakerandroid.decoder.api.Apk;

import java.io.IOException;

public abstract class Transform {
    protected Apk apk;
    protected AndroidProject androidProject;
    public Transform(Apk apk, AndroidProject androidProject){
        this.apk = apk;
        this.androidProject = androidProject;
    }
    public abstract boolean transform(TransformInvocation transformInvocation) ;
}
