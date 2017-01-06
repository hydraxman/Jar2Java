package com.bryansharp.jar2java;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by bushaopeng on 17/1/6.
 */
public class Main {
    private static File baseDir;

    public static void main(String[] args) {
        if (args == null || args.length < 1) {
            log("please specify a jar file");
            return;
        }
        try {
            ArrayList<String> paths = new ArrayList<>();
            for (String jarFile : args) {
                if (checkJar(jarFile)) {
                    paths.add(jarFile);
                }
            }
            if(paths.size()>0){
                initBuildPath();
                for (String jarFile : paths) {
                    printJar(jarFile);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initBuildPath() {
        String dirName = getOutputFileDirName();
        baseDir = new File(dirName);
        baseDir.mkdirs();
    }

    private static boolean checkJar(String jarPath) {
        return !(jarPath == null || !jarPath.endsWith(".jar") || !new File(jarPath).exists());
    }

    private static void printJar(String jarFullPath) throws IOException {
        ZipFile zipFile = new ZipFile(jarFullPath);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = entries.nextElement();
            String name = zipEntry.getName();
            if (name.endsWith(".class")) {
//                log("start to show the code of " + name + "\n=======\ndisplayed as below:\n=======\n");
                String result = Decompiler.decompile(jarFullPath, name);
//                log(result);
                stringToClassFile(name, result);
            }
        }
    }

    private static void stringToClassFile(String name, String result) throws IOException {
        int endIndex = name.lastIndexOf("/");
        String dirName = baseDir.getAbsolutePath() + "/" + name.substring(0, endIndex);
        File dir = new File(dirName);
        dir.mkdirs();
        String javaFilename = name.substring(endIndex + 1).replace(".class", ".java");
        File javaFile = new File(dir, javaFilename);
        FileOutputStream fileOutputStream = new FileOutputStream(javaFile);
        fileOutputStream.write(result.getBytes());
        fileOutputStream.flush();
        javaFile.setWritable(true, false);
    }

    final static String pathname = "build/javaDirOutput";

    private static String getOutputFileDirName() {
        if (new File(pathname).exists()) {
            return getNewDirName(1);
        }
        return pathname;
    }

    private static String getNewDirName(int count) {
        if (new File(pathname + count).exists()) {
            return getNewDirName(++count);
        }
        return pathname + count;
    }

    public static void log(Object msg) {
        System.out.println(msg.toString());
    }
}
