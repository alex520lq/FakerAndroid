package com.fakerandroid.decoder.transforms;

import com.fakerandroid.decoder.api.AndroidProject;
import com.fakerandroid.decoder.il2cpp.Il2cppBinary;
import com.fakerandroid.decoder.api.Apk;
import com.fakerandroid.decoder.pipeline.Transform;
import com.fakerandroid.decoder.pipeline.TransformInvocation;

public class Il2cppDumper extends Transform  {
    public Il2cppDumper(Apk apk, AndroidProject androidProject) {
        super(apk, androidProject);
    }

    @Override
    public boolean transform(TransformInvocation transformInvocation) {
        transformInvocation.callBack("Checking or generating il2cppscafoding...");
        try {
            Il2cppBinary.dumpIl2cpp(apk.getApkFile(),androidProject.getCpp());
        } catch (Exception e) {
            transformInvocation.callBack("exception while excute il2cpp binary");
            e.printStackTrace();
        }
        return true;
    }
}
