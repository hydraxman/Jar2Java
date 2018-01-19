package com.bryansharp.jar2java;

import com.bryansharp.jar2java.analyze.JarAnalyzer;
import com.bryansharp.jar2java.convert.Decompiler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by bushaopeng on 17/1/6.
 */
public class Main {
    final static String pathname = "build/javaDirOutput";
    private static File baseDir;

    public static void main(String[] args) {
//        AidlProcessor processor = new AidlProcessor();
//        if (processor.process("/Users/bushaopeng/Desktop/myGit/JDroid/src/main/java")) {
//            return;
//        }
//        JavapJarParser jarParser = new JavapJarParser();
//        if (jarParser.parse("/Users/bushaopeng/IdeaProjects/Jar2Java/classes.jar")) {
//            return;
//        }
        JarAnalyzer jarAnalyzer = new JarAnalyzer();
        if (jarAnalyzer.getReproguardMapping(args[0]) != null) {
            return;
        }
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
            if (paths.size() > 0) {
                initBuildPath();
                for (String jarFile : paths) {
                    decompileJar(jarFile);
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

    private static void decompileJar(String jarFullPath) throws IOException {
        boolean needRename = true;
        if (needRename) {
            JarAnalyzer jarAnalyzer = new JarAnalyzer();

            Map<String, String> renameMap = jarAnalyzer.getRenameMap(jarFullPath);
            for (Map.Entry<String, String> entry : renameMap.entrySet()) {
                Utils.log(entry.getKey() + "->" + entry.getValue());
            }
            File file = jarAnalyzer.renameClassInJar(jarFullPath, renameMap);
            jarFullPath = file.getAbsolutePath();
        }

        ZipFile zipFile = new ZipFile(jarFullPath);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = entries.nextElement();
            String name = zipEntry.getName();
            if (name.endsWith(".class")) {
                //内部类直接跳过
                if (name.contains("$")) {
                    log("jump inner class " + name);
                    continue;
                }
                String result = null;
                log("start to decompile class " + name);
                try {
                    result = Decompiler.decompile(jarFullPath, name);
                    stringToClassFile(name, result);
                } catch (Exception e) {
                    logError("decompile " + name + " failed");
                    e.printStackTrace();
                }
            }
        }
    }

    private static void stringToClassFile(String name, String result) throws IOException {
        if (name == null) return;
        if (result == null) return;
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
        if (msg == null) {
            return;
        }
        System.out.println(msg.toString());
    }

    public static void logError(Object msg) {
        if (msg == null) {
            return;
        }
        System.err.println(msg.toString());
    }

    public static void logDiv() {
        System.out.println("==============================");
    }

    public static void logEach(Object... msgs) {
        logDiv();
        for (Object msg : msgs) {
            System.out.print(msg.toString());
            System.out.print("\t");
        }
        System.out.print("\n");
        logDiv();
    }

}
