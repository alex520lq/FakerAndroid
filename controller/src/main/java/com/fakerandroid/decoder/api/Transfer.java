package com.fakerandroid.decoder.api;
import com.fakerandroid.decoder.pipeline.TransformInvocation;
import com.fakerandroid.decoder.pipeline.TransformManager;
import com.fakerandroid.decoder.transforms.*;

import java.io.File;

public class Transfer {
    private File in;
    private File out;
    TransformInvocation transformInvocation;
    public Transfer(String inPath,String outPath,TransformInvocation transformInvocation){
        in = new File(inPath);
        out = new File(outPath);
        this.transformInvocation = transformInvocation;
    }
    public void translate(){
        TransformManager transformManager = new TransformManager(transformInvocation);
        Apk apk = new Apk(in);
        AndroidProject androidProject = new AndroidProject(out);
        System.out.println(apk.getApkFile().getAbsolutePath());
        if(!in.exists()){
            transformInvocation.callBack("In file not exist..");
            return;
        }
        if(!in.getAbsolutePath().endsWith(".apk")){
            transformInvocation.callBack("not a apk file..");
        }
        //Decoder
        transformManager.addTransform(new ResourceProcesser(apk,androidProject));
        transformManager.addTransform(new DexToSmali(apk,androidProject));
        transformManager.addTransform(new DexToJar(apk,androidProject));
        transformManager.addTransform(new Il2cppDumper(apk,androidProject));

        //Merge
        transformManager.addTransform(new RuntimeBaseMerge(apk,androidProject));
        transformManager.addTransform(new RuntimeIl2cppMerge(apk,androidProject));

        //fix
        transformManager.addTransform(new Project(apk,androidProject));
        transformManager.action();
    }
}
