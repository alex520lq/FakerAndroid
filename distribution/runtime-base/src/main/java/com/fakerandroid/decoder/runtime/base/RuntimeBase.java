package com.fakerandroid.decoder.runtime.base;

import com.fakerandroid.decoder.util.PatchUtil;

import java.io.File;
import java.io.IOException;

public class RuntimeBase {
    public static void mergeRuntimeLibsCpp(File out){
//        PatchUtil.copyDirFromJar("/libs/cpp",out.getAbsolutePath());
    }

    public static void mergeRuntimeLibsJava(File out) throws IOException {
        PatchUtil.copyDirFromJar("/libs/java",out.getAbsolutePath());
    }

    public static void mergeRuntimeJavaCode(File out) throws IOException {
        PatchUtil.copyDirFromJar("/template/java",out.getAbsolutePath());
    }

    public static void mergeRuntimeCppCode(File out) throws IOException {
        PatchUtil.copyDirFromJar("/template/cpp",out.getAbsolutePath());
    }
}
