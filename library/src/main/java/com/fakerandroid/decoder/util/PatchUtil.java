package com.fakerandroid.decoder.util;
import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PatchUtil {

    //平行拷贝目录
    public static void copyDirFromJar(String folderPath,String toFolderPath) throws IOException {
        loadRecourseFromJarByFolder(folderPath,toFolderPath, PatchUtil.class,null);
    }
    public static void loadRecourseFromJarByFolder(String folderPath,String targetFolderPath ,Class clazz,String basePath) throws IOException {
        URL url = clazz.getResource(folderPath);
        if(basePath==null){
            basePath = url.getPath();
        }
        URLConnection urlConnection = url.openConnection();
        if (urlConnection instanceof JarURLConnection) {
            copyJarResources((JarURLConnection) urlConnection,folderPath,targetFolderPath,clazz,basePath);
        }else{
            copyFileResources(url, folderPath,targetFolderPath,clazz,basePath);
        }
    }
    private static void copyFileResources(URL url, String folderPath,String targetFolderPath, Class clazz,String basePath) throws IOException {
        File root = new File(url.getPath());
        System.out.println("basePath"+ basePath);
        if (root.isDirectory()) {
            File[] files = root.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    loadRecourseFromJarByFolder(folderPath + "/" + file.getName(),targetFolderPath,clazz,basePath);
                } else {
                    String toPath = file.getPath().replace(new File(basePath).getAbsolutePath(),targetFolderPath);
                    loadRecourseFromJar(folderPath + "/" + file.getName(),toPath,clazz);
                }
            }
        }
    }
    /**
     *
     * @param jarURLConnection
     * @throws IOException
     */
    private static void copyJarResources(JarURLConnection jarURLConnection,String folderPath,String targetFolderPath,Class clazz,String basePath) throws IOException {
        JarFile jarFile = jarURLConnection.getJarFile();
//        String baseUrl = jarURLConnection.getURL().getPath();
//        System.out.println("base url "+baseUrl);
        Enumeration<JarEntry> entrys = jarFile.entries();
        while (entrys.hasMoreElements()) {
            JarEntry entry = entrys.nextElement();
            if (entry.getName().startsWith(jarURLConnection.getEntryName()) && !entry.getName().endsWith("/")) {
                String target = ("/"+entry.getName()).replace(folderPath,"");
//                String pathTarget = target.replace("/","\\");
                File file  = new File(targetFolderPath,target);
                String toPath = file.getAbsolutePath();
                loadRecourseFromJar("/" + entry.getName(),toPath,clazz);
            }
        }
        jarFile.close();
    }
    public static void loadRecourseFromJar(String path,String toPath,Class clazz) throws IOException {
        if (!path.startsWith("/")) {
            throw new IllegalArgumentException("The path has to be absolute (start with '/').");
        }

        if (path.endsWith("/")) {
            throw new IllegalArgumentException("The path has to be absolute (cat not end with '/').");
        }

//        int index = toPath.lastIndexOf('/');
//        String filename = recourseFolder.substring(index + 1);
        String folderPath = new File(toPath).getParent();
        // If the folder does not exist yet, it will be created. If the folder
        // exists already, it will be ignored
        File dir = new File(folderPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        if(toPath.endsWith(".tmpl")){
            toPath = getFileNameNoEx(toPath);
        }
        // If the file does not exist yet, it will be created. If the file
        // exists already, it will be ignored
//        filename = folderPath + filename;
        File file = new File(toPath);

        if (!file.exists() && !file.createNewFile()) {
            return;
        }

        // Prepare buffer for data copying
        byte[] buffer = new byte[1024];
        int readBytes;

        // Open and check input stream
        URL url = clazz.getResource(path);
        URLConnection urlConnection = url.openConnection();
        InputStream is = urlConnection.getInputStream();

        if (is == null) {
            throw new FileNotFoundException("File " + path + " was not found inside JAR.");
        }
        OutputStream os = new FileOutputStream(file);
        try {
            while ((readBytes = is.read(buffer)) != -1) {
                os.write(buffer, 0, readBytes);
            }
        } finally {
            os.close();
            is.close();
        }

    }
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
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
}
