package com.fakerandroid.decoder.transforms;

import com.fakerandroid.decoder.api.AndroidProject;
import com.fakerandroid.decoder.api.Apk;
import com.fakerandroid.decoder.pipeline.Transform;
import com.fakerandroid.decoder.pipeline.TransformInvocation;
import com.fakerandroid.decoder.project.ProjectMerge;
import com.fakerandroid.decoder.util.FileUtils;
import com.fakerandroid.decoder.util.PatchUtil;
import com.fakerandroid.decoder.util.TextUtil;
import java.io.File;
import java.io.IOException;

public class Project extends Transform {
    public Project(Apk apk, AndroidProject androidProject) {
        super(apk, androidProject);
    }
    @Override
    public boolean transform(TransformInvocation transformInvocation) {
        transformInvocation.callBack("Android studio project fomarting....");
        try {
            ProjectMerge.copyProject(androidProject.getProject());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        fixProject(androidProject);
        transformInvocation.callBack("You have faked a android studio project from apk!");
        transformInvocation.callBack("Generated project path:"+androidProject.getProject().getAbsolutePath()+".");
        return true;
    }

    private void fixProject(AndroidProject androidProject){

        AndroidProject.ManifestInfo manifestInfo = (AndroidProject.ManifestInfo) androidProject.getIntermediate(AndroidProject.INTERMEDIATE_MANIFESTINFO);
        File appBuild = androidProject.getAppBuild();
        //pkg
        try {
            FileUtils.autoReplaceStr(appBuild,"{pkg}",manifestInfo.getPakcageName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //ABI
        String abiStr = "";
        File targetjniLibs = androidProject.getjniLibs();
        File jniLibsARMV7A = new File(targetjniLibs,"armeabi-v7a");
        File armeabi = new File(targetjniLibs,"armeabi");
        if(armeabi.exists()&&!jniLibsARMV7A.exists()){
            armeabi.renameTo(jniLibsARMV7A);
        }
        if(jniLibsARMV7A.exists()){
            if(TextUtil.isEmpty(abiStr)){
                abiStr = "'armeabi-v7a'";
            }else {
                abiStr = abiStr+",'armeabi-v7a'";
            }
        }
        File jniLibsARM64V8A = new File(targetjniLibs,"arm64-v8a");
        if(jniLibsARM64V8A.exists()){
            if(TextUtil.isEmpty(abiStr)){
                abiStr = "'arm64-v8a'";
            }else {
                abiStr = abiStr+",'arm64-v8a'";
            }
        }
        if(!jniLibsARMV7A.exists()&&!jniLibsARM64V8A.exists()&&!armeabi.exists()){
            abiStr = "'armeabi-v7a','arm64-v8a'";
        }
        File jniLibsX86 = new File(targetjniLibs,"x86");

        if(jniLibsX86.exists()){
            abiStr = abiStr+",'x86'";
        }

        File jniLibsX86_64 = new File(targetjniLibs,"x86_64");
        if(jniLibsX86_64.exists()){
            abiStr = abiStr+",'x86_64'";
        }

        try {
            FileUtils.autoReplaceStr(appBuild,"{abi}",abiStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //buildconfig

        try {
            FileUtils.autoReplaceStr(appBuild,"{versionCode}",manifestInfo.getVersionCode()!=null?manifestInfo.getVersionCode():"1");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileUtils.autoReplaceStr(appBuild,"{versionName}",manifestInfo.getVersionName()!=null?manifestInfo.getVersionName():"0.01");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String minSdkVersion = manifestInfo.getMinSdkVersion();
        //FileUtil.autoReplaceStr(gameBuildGrandle,"{minSdkVersion}",minSdkVersion);
        String targetSdkVersion =manifestInfo.getTargetSdkVersion();

        if(TextUtil.isEmpty(targetSdkVersion)){
            try {
                FileUtils.autoReplaceStr(appBuild,"{targetSdkVersion}","26");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            try {
                FileUtils.autoReplaceStr(appBuild,"{targetSdkVersion}",targetSdkVersion);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
