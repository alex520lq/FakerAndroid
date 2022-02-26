package com.fakerandroid.decoder.dex2jar;

import com.googlecode.d2j.reader.DexFileReader;
import com.googlecode.dex2jar.tools.BaksmaliBaseDexExceptionHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

public class Dex2jar {

    private static Logger logger = Logger.getLogger("Dex2jar");
    public static void toJar(File apk,File out)  {
        TreeMap<String, DexFileReader> fileReaderTreeMap = null;
        try {
            fileReaderTreeMap = PatchMultiDexFileReader.open(Files.readAllBytes(apk.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        BaksmaliBaseDexExceptionHandler handler = false ? null : new BaksmaliBaseDexExceptionHandler();
        Iterator it = fileReaderTreeMap.entrySet().iterator();
        File outPath = new File(out,"classes.all.dex.jar");
        if(outPath.exists()){
            outPath.delete();
        }
        while (it.hasNext()){
            Map.Entry<String, DexFileReader>  entry = (Map.Entry) it.next();
            DexFileReader dexFileReader = entry.getValue();
            try {
                PatchDex2jar.from(dexFileReader).withExceptionHandler(handler).reUseReg(false).topoLogicalSort()
                        .skipDebug(false).optimizeSynchronized(false).printIR(false)
                        .noCode(false).skipExceptions(false).to(outPath.toPath());
            } catch (IOException e) {
                logger.info("dex2jar fail :"+dexFileReader.getClassSize());
                e.printStackTrace();
            }
        }
    }
}
