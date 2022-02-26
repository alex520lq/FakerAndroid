package com.fakerandroid.decoder.transforms;

import com.fakerandroid.decoder.api.AndroidProject;
import com.fakerandroid.decoder.dex2jar.Dex2jar;
import com.fakerandroid.decoder.api.Apk;
import com.fakerandroid.decoder.pipeline.Transform;
import com.fakerandroid.decoder.pipeline.TransformInvocation;

import java.io.File;

public class DexToJar extends Transform {

    public DexToJar(Apk apk, AndroidProject androidProject) {
        super(apk, androidProject);
    }

    @Override
    public boolean transform(TransformInvocation transformInvocation) {
        transformInvocation.callBack("Translating dexes to java scaffodding jar....");
        try {
            File javaScaffoding = androidProject.getJavaScaffoding();
            if(javaScaffoding.exists()){
                javaScaffoding.delete();
            }
            javaScaffoding.mkdirs();
            Dex2jar.toJar(apk.getApkFile(),javaScaffoding);
        }catch (Exception e){
            transformInvocation.callBack("Translating dexes to java scaffodding jar happen exception....");
        }
        return true;
    }
}
