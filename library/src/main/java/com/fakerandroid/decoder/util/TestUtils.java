package com.fakerandroid.decoder.util;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class TestUtils {
    private static final String TEST_SAMPLES_DIR = "test-samples/";
    private static final String TEST_SAMPLES_DIRP_OUT = "test-samples-out/";

    public static File getFileFromSampleDir(String fileName) {
        URL resource = TestUtils.class.getClassLoader().getResource(TEST_SAMPLES_DIR + fileName);
        String pathStr = resource.getFile();
        return new File(pathStr);
    }
    public static File getFileOutSampleDir(String testCase,String fileName) throws IOException {
        File caseDir =new File(TEST_SAMPLES_DIRP_OUT,testCase);
        File outDir = new File(caseDir,fileName);
        if(outDir.exists()){
            FileUtils.deleteDir(outDir);
        }
        outDir.mkdirs();
        return outDir;
    }
}
