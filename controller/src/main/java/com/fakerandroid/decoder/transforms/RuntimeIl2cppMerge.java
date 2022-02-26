package com.fakerandroid.decoder.transforms;

import com.fakerandroid.decoder.api.AndroidProject;
import com.fakerandroid.decoder.api.Apk;
import com.fakerandroid.decoder.pipeline.Transform;
import com.fakerandroid.decoder.pipeline.TransformInvocation;
import com.fakerandroid.decoder.rutime.il2cpp.RuntimeIl2cpp;
import com.fakerandroid.decoder.util.FileUtils;
import java.io.File;
import java.io.IOException;

public class RuntimeIl2cppMerge extends Transform {

    public RuntimeIl2cppMerge(Apk apk, AndroidProject androidProject) {
        super(apk, androidProject);
    }

    @Override
    public boolean transform(TransformInvocation transformInvocation)  {
        File il2cppScaffoldingARM = new File(androidProject.getCpp(),"Il2cpp-Scaffolding-ARM");
        File il2cppScaffoldingARM64 = new File(androidProject.getCpp(),"Il2cpp-Scaffolding-ARM64");
        if(!il2cppScaffoldingARM.exists()&&!il2cppScaffoldingARM64.exists()) {
            return true;
        }
        transformInvocation.callBack("Runtime il2cpp merging...");
        try {
            RuntimeIl2cpp.mergeRuntimeIl2cppCpp(androidProject.getCpp());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        try {
            RuntimeIl2cpp.mergeRuntimeIl2cppJava(androidProject.getJava());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        fixTmplCode(androidProject);
        return true;
    }

    private void fixTmplCode(AndroidProject androidProject) {
        AndroidProject.ManifestInfo manifestInfo = (AndroidProject.ManifestInfo) androidProject.getIntermediate(AndroidProject.INTERMEDIATE_MANIFESTINFO);
        File fakerActivityFile = new File(androidProject.getJava(),"com/fakerandroid/boot/FakerActivity.java");
        try {
            FileUtils.autoReplaceStr(fakerActivityFile,"{R}",manifestInfo.getPakcageName()+".R");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
