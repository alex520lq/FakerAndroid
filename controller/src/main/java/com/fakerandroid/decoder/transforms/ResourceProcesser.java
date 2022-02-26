package com.fakerandroid.decoder.transforms;

import brut.androlib.meta.MetaInfo;
import com.fakerandroid.decoder.api.AndroidProject;
import com.fakerandroid.decoder.apktool.Resources;
import com.fakerandroid.decoder.api.Apk;
import com.fakerandroid.decoder.pipeline.Transform;
import com.fakerandroid.decoder.pipeline.TransformInvocation;
import com.fakerandroid.decoder.util.FileUtils;
import com.fakerandroid.decoder.util.ManifestEditor;
import org.dom4j.DocumentException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResourceProcesser extends Transform {
    @Override
    public boolean transform(TransformInvocation transformInvocation) {
        transformInvocation.callBack("Deocoding the apk file...");
        File out = androidProject.getMain();
        if(out.exists()){
            FileUtils.deleteDir(out);
        }
        out.mkdirs();
        Resources.decode(apk.getApkFile(),out);
        project(androidProject);
        discard(androidProject);
        meta(androidProject);
        fixRes(androidProject);
        return true;
    }

    public ResourceProcesser(Apk apk, AndroidProject androidProject) {
        super(apk, androidProject);
    }

    private void meta(AndroidProject androidProject) {
        File manifest = androidProject.getAndroidManifest();
        AndroidProject.ManifestInfo manifestInfo = new AndroidProject.ManifestInfo();
        MetaInfo metaInfo = null;
        try {
            metaInfo = MetaInfo.load(new FileInputStream(androidProject.getYmlFile()));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        ManifestEditor manifestEditor = null;
        try {
            manifestEditor = new ManifestEditor(manifest);
            String packageName = manifestEditor.getPackagenName();
            manifestInfo.setPakcageName(packageName);
            String applicationName = manifestEditor.getApplicationName();
            manifestInfo.setApplicationName(applicationName);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        if(metaInfo!=null){
            manifestInfo.setVersionCode(metaInfo.versionInfo.versionCode);
            manifestInfo.setVersionName(metaInfo.versionInfo.versionName);
            manifestInfo.setMinSdkVersion(metaInfo.sdkInfo.get("minSdkVersion"));
            manifestInfo.setTargetSdkVersion(metaInfo.sdkInfo.get("targetSdkVersion"));
        }
        androidProject.addIntermediate(AndroidProject.INTERMEDIATE_MANIFESTINFO,manifestInfo);
    }

    private void discard(AndroidProject androidProject) {
        File resources = androidProject.getMain();
        List<File> fs = Arrays.asList(resources.listFiles());
        List<String> dexnames = new ArrayList<>();
        for (File f:fs){
            String fn = f.getName();
            if(fn.startsWith("classes")&&fn.endsWith(".dex")) {
                dexnames.add(fn);
                f.delete();
            }
        }
        androidProject.addIntermediate(AndroidProject.INTERMEDIATE_DEX_NAMES,dexnames);
    }

    private void project(AndroidProject androidProject) {//TODO Rename
        File resources = androidProject.getMain();
        if(!resources.exists()){
            return;
        }
        File libResources = new File(resources,"lib");
        if(libResources.exists()){
            libResources.renameTo(androidProject.getjniLibs());
        }

        File unknownResources = new File(resources,"unknown");
        if(unknownResources.exists()){
            unknownResources.renameTo(androidProject.getResources());
        }

        File originalResources = new File(resources,"original");
        if(originalResources.exists()){
            File assets = androidProject.getAssets();
            if(!assets.exists()){
                assets.mkdirs();
            }
            originalResources.renameTo(new File(assets,"original"));
        }
    }
    void fixRes(AndroidProject androidProject) {
        try {
            File fileRes = androidProject.getRes();
            func(fileRes,fileRes);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void func(File res, File file){
        File[] fs = file.listFiles();
        for(File f:fs){
            if(f.isDirectory())	//
                func(res,f);
            if(f.isFile()){
                if(f.getName().startsWith("$")){
                    File fixName = new File(f.getParent(),f.getName().replace("$",""));
                    funcRe(res,getFileNameNoEx(f.getName()),getFileNameNoEx(fixName.getName()));
                    f.renameTo(fixName);
                }
            }
        }
    }
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }
    private static void funcRe(File file, String oStr, String nStr){
        File[] fs = file.listFiles();
        for(File f:fs){
            if(f.isDirectory())	//
                funcRe(f,oStr,nStr);
            if(f.isFile()){
                try {
                    if(f.getName().endsWith(".xml")){
                        FileUtils.autoReplaceStr(f,oStr,nStr);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
