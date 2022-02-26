package com.fakerandroid.decoder.dex2jar;

import com.googlecode.d2j.reader.DexFileReader;
import com.googlecode.d2j.util.zip.AccessBufByteArrayOutputStream;
import com.googlecode.d2j.util.zip.ZipEntry;
import com.googlecode.d2j.util.zip.ZipFile;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.TreeMap;

public class PatchMultiDexFileReader {

    public static TreeMap<String, DexFileReader> open(byte[] data) throws IOException {
        TreeMap<String, DexFileReader> dexFileReaders = new TreeMap();
        if (data.length < 3) {
            throw new IOException("File too small to be a dex/zip");
        } else if("PK".equals(new String(data, 0, 2, StandardCharsets.ISO_8859_1))) {

            ZipFile zipFile = new ZipFile(data);
            Throwable var3 = null;

            try {
                Iterator var4 = zipFile.entries().iterator();

                while(var4.hasNext()) {
                    ZipEntry e = (ZipEntry)var4.next();
                    String entryName = e.getName();
                    if (entryName.startsWith("classes") && entryName.endsWith(".dex") && !dexFileReaders.containsKey(entryName)) {
                        dexFileReaders.put(entryName, new DexFileReader(toByteArray(zipFile.getInputStream(e))));
                    }
                }
            } catch (Throwable var14) {
                var3 = var14;
                throw var14;
            } finally {
                if (zipFile != null) {
                    if (var3 != null) {
                        try {
                            zipFile.close();
                        } catch (Throwable var13) {
                            var3.addSuppressed(var13);
                        }
                    } else {
                        zipFile.close();
                    }
                }

            }

            if (dexFileReaders.size() == 0) {
                throw new IOException("Can not find classes.dex in zip file");
            } else {
                return dexFileReaders;
            }
        } else {
            throw new IOException("the src file not a .dex or zip file");
        }
    }

    private static byte[] toByteArray(InputStream is) throws IOException {
        AccessBufByteArrayOutputStream out = new AccessBufByteArrayOutputStream();
        byte[] buff = new byte[1024];

        for(int c = is.read(buff); c > 0; c = is.read(buff)) {
            out.write(buff, 0, c);
        }

        return out.getBuf();
    }
}
