package com.fakerandroid.decoder.apktool;

import brut.androlib.ApkDecoder;

import java.io.File;

public class Resources {
    public static void decode(File in, File out) {
        ApkDecoder decoder = new ApkDecoder();
        try {
            decoder.setDecodeSources((short) 0);
            decoder.setOutDir(out);
            decoder.setForceDelete(true);
            decoder.setApkFile(in);
            decoder.decode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
