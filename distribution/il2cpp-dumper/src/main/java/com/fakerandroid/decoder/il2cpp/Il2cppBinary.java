package com.fakerandroid.decoder.il2cpp;

import com.fakerandroid.decoder.exception.BinaryException;
import com.fakerandroid.decoder.util.Jar;
import com.fakerandroid.decoder.util.OS;
import com.fakerandroid.decoder.util.OSDetection;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Il2cppBinary {

    private static  File getIl2cppBinary() throws BinaryException {
        File il2cppBinary=null;
        if (OSDetection.isMacOSX()) {
            il2cppBinary = Jar.getResourceAsFile("/prebuilt/macosx/"+"il2cpp", Il2cppBinary.class);
        } else if (OSDetection.isUnix()) {
            il2cppBinary = Jar.getResourceAsFile("/prebuilt/linux/"+"il2cpp", Il2cppBinary.class);
        } else if (OSDetection.isWindows()) {
            il2cppBinary = Jar.getResourceAsFile("/prebuilt/windows/"+"il2cpp.exe", Il2cppBinary.class);
        }
        if(il2cppBinary!=null){
            il2cppBinary.setExecutable(true);
        }
        return il2cppBinary;
    }

    public static void dumpIl2cpp(File in,File out) throws Exception {
        List<String> il2cppBinaryCommad = new ArrayList<>();
        File il2cppBinary = getIl2cppBinary();
        if(il2cppBinary==null){
            throw  new RuntimeException("null il2cppBinary file");
        }
        il2cppBinaryCommad.add(il2cppBinary.getAbsolutePath());

        il2cppBinaryCommad.add("-i");
        il2cppBinaryCommad.add(in.getAbsolutePath());

        File il2cppScaffolding = new File(out.getAbsolutePath(),"Il2cpp-Scaffolding");
        il2cppBinaryCommad.add("-h");
        il2cppBinaryCommad.add(il2cppScaffolding.getAbsolutePath());

        il2cppBinaryCommad.add("-e");
        il2cppBinaryCommad.add("none");

        File il2cppScaffoldingHelper = new File(out.getAbsolutePath(),"Il2cppScaffoldingHelper");

        il2cppBinaryCommad.add("-c");
        il2cppBinaryCommad.add(new File(il2cppScaffoldingHelper,"help.cs").getAbsolutePath());

        il2cppBinaryCommad.add("-p");
        il2cppBinaryCommad.add(new File(il2cppScaffoldingHelper,"help.py").getAbsolutePath());

        il2cppBinaryCommad.add("-o");
        il2cppBinaryCommad.add(new File(il2cppScaffoldingHelper,"help.json").getAbsolutePath());

        il2cppBinaryCommad.add("--cpp-compiler");
        il2cppBinaryCommad.add("GCC");


        try {
            OS.exec(il2cppBinaryCommad.toArray(new String[0]));
            File scaffolding_ARM = new File(out,"Il2cpp-Scaffolding-ARM");
            if(scaffolding_ARM.exists()){
                formatScaffolding(scaffolding_ARM);
            }
            File Scaffolding_ARM64 = new File(out,"Il2cpp-Scaffolding-ARM64");
            if(Scaffolding_ARM64.exists()){
                formatScaffolding(Scaffolding_ARM64);
            }
            File il2cppScaffoldingx86 = new File(out,"Il2cpp-Scaffolding-x86");
            if(il2cppScaffoldingx86.exists()){
                formatScaffolding(il2cppScaffoldingx86);
            }
            File il2cppScaffoldingx64 = new File(out,"Il2cpp-Scaffolding-x64");
            if(il2cppScaffoldingx64.exists()){
                formatScaffolding(il2cppScaffoldingx64);
            }
        } catch (Exception e) {
            //
        }
    }
    private static void  formatScaffolding(File scaffolding) throws IOException {
        if(scaffolding.exists()&&scaffolding.isDirectory()){
            File scaffolding_ARM_spam[] =scaffolding.listFiles();

            for (File f:scaffolding_ARM_spam ) {//删除无用文件
                if(!f.isDirectory()){
                    f.delete();
                }
            }
            File userDir = new File(scaffolding,"user");
            delete(userDir);
            File frameworkDir = new File(scaffolding,"framework");
            delete(frameworkDir);
            File appdataDir = new File(scaffolding,"appdata");
            File appdataFils[] = appdataDir.listFiles();
            for (File f:appdataFils) {
                f.renameTo(new File(scaffolding,f.getName()));
            }
            delete(appdataDir);
        }
    }
    public static void delete(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                delete(f);
            }
        }
        file.delete();
    }
}
