package com.bryansharp.jar2java;

import com.bryansharp.jar2java.analyze.JarAnalyzer;
import com.bryansharp.jar2java.convert.Decompiler;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

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
//        try {
//            String stringInAMobi = decodeStringInAMobi("".getBytes("UTF-8"));
//            System.out.println(stringInAMobi);
//            stringInAMobi = decodeStringInAMobi("qM3KeU1CoM1Bkl==".getBytes("UTF-8"));
//            System.out.println(stringInAMobi);
//            stringInAMobi = decodeStringInAMobi("q93KbaJKq7S=".getBytes("UTF-8"));
//            System.out.println(stringInAMobi);
//            stringInAMobi = decodeStringInAMobi("q737kaFgkaT=".getBytes("UTF-8"));
//            System.out.println(stringInAMobi);
//            stringInAMobi = decodeStringInAMobi("qM3EkSFgHM1-rM1iWX==".getBytes("UTF-8"));
//            System.out.println(stringInAMobi);
//            stringInAMobi = decodeStringInAMobi("rtY-q7AjkVYjHBbzHBlErt/KotAEDzuulKwubK3Re-3/Nmk1bX==".getBytes("UTF-8"));
//            System.out.println(stringInAMobi);
//            stringInAMobi = decodeStringInAMobi("rtY-q7AjkVYjHBbzHBlErt/KotAEDzuulKwubK3RlebSbel=".getBytes("UTF-8"));
//            System.out.println(stringInAMobi);
//            stringInAMobi = decodeStringInAMobi("WClPrf3KafriDBuQqX==".getBytes("UTF-8"));
//            System.out.println(stringInAMobi);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }


//        extractAllSources();
//        filterJar();
        readAllConstant();
//        Map<String, VisitedClass> visitedClassMap = jarAnalyzer.analyzeJar(args[0]);
//        if (jarAnalyzer.extractSource(args[0])) {
//            return;
//        }
//        if (args == null || args.length < 1) {
//            log("please specify a jar file");
//            return;
//        }
//        try {
//            ArrayList<String> paths = new ArrayList<>();
//            for (String jarFile : args) {
//                if (checkJar(jarFile)) {
//                    paths.add(jarFile);
//                }
//            }
//            if (paths.size() > 0) {
//                initBuildPath();
//                for (String jarFile : paths) {
//                    String jarFullPath = jarFile;
//                    boolean needRename = false;
//                    if (needRename) {
//                        JarModifier jarModifier = new JarModifier();
//
//                        Map<String, String> renameMap = jarModifier.getRenameMap(jarFullPath);
//                        File file = jarModifier.renameClassInJar(jarFullPath, renameMap);
//                        jarFullPath = file.getAbsolutePath();
//                    }
//                    turnJarIntoJava(jarFullPath);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private static void filterJar() {
        String[] args;
        args = new String[]{
                "/Users/bushaopeng/IdeaProjects/Jar2Java/classes2-dex2jar.jar"
        };

        JarAnalyzer jarAnalyzer = new JarAnalyzer();
        for (String arg : args) {
            jarAnalyzer.filterJar(arg);
        }
    }

    private static void extractAllSources() {
        String[] args;
        args = new String[]{
                "/Users/bushaopeng/IdeaProjects/Jar2Java/classes2-dex2jar.jar"
        };

        JarAnalyzer jarAnalyzer = new JarAnalyzer();
        for (String arg : args) {
            jarAnalyzer.extractSource(arg);
        }
    }

    private static void readAllConstant() {
        String[] args;
//        args = new String[]{"/Users/bushaopeng/IdeaProjects/Jar2Java/sdk-4.3.0.5.2.aar",
//                "/Users/bushaopeng/IdeaProjects/Jar2Java/sdk-4.3.0.5.aar",
//                "/Users/bushaopeng/IdeaProjects/Jar2Java/sdk-4.3.1.1.4230.13.aar",
//                "/Users/bushaopeng/IdeaProjects/Jar2Java/sdk-5.0.0.0.4230-adserverDebug.aar",
//                "/Users/bushaopeng/IdeaProjects/Jar2Java/sdk-5.0.0.4.aar",
//                "/Users/bushaopeng/IdeaProjects/Jar2Java/sdk-5.0.0.6.aar",
//                "/Users/bushaopeng/IdeaProjects/Jar2Java/sdk-5.1.0.0.16.aar"
//        };
        args = new String[]{"/Users/bushaopeng/IdeaProjects/Jar2Java/altamob-5.2.0.Q.jar"
        };

        JarAnalyzer jarAnalyzer = new JarAnalyzer();
        for (String arg : args) {
            jarAnalyzer.readAllConstant(arg);
        }
    }

    public static void initBuildPath(String defaultName) {
        String dirName = null;
        if (StringUtils.isEmpty(defaultName)) {
            dirName = getOutputFileDirName();
        } else {
            dirName = "build/" + defaultName;
        }
        baseDir = new File(dirName);
        baseDir.mkdirs();
    }

    private static boolean checkJar(String jarPath) {
        return !(jarPath == null || !jarPath.endsWith(".jar") || !new File(jarPath).exists());
    }


    public static void turnJarIntoJava(String jarFullPath) throws IOException {
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

    public static String decodeStringInAMobi(String source) {
        try {
            return decodeStringInAMobi(source.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decodeStringInAMobi(byte[] source) {
        try {
            int len = source.length;
            int j = 0;
            int k = 0;
            int m = 0;
            byte[] decode = new byte[len * 3 / 4];
            byte[] dict = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 36, 0, 13, 42, 5,
                    58, 21, 62, 63, 15, 38, 32, 7, 0, 0, 0, 0, 0, 0, 0, 61, 39, 35, 11, 46, 9, 56, 27, 48, 33,
                    52, 34, 54, 19, 44, 47, 40, 31, 4, 8, 6, 2, 29, 0, 57, 59, 0, 0, 0, 0, 0, 0, 23, 17, 12, 49,
                    20, 55, 50, 43, 51, 41, 25, 16, 53, 18, 26, 30, 28, 24, 3, 22, 1, 60, 45, 10, 14, 37, 0, 0, 0, 0, 0};

            while ((j + 4 <= len) &&
                    ((k = dict[(source[j] & 0xFF)] << 18 |
                            dict[(source[(j + 1)] & 0xFF)] << 12 |
                            dict[(source[(j + 2)] & 0xFF)] << 6 |
                            dict[(source[(j + 3)] & 0xFF)]) >= 0)) {
                decode[(m + 2)] = ((byte) k);
                decode[(m + 1)] = ((byte) (k >> 8));
                decode[m] = ((byte) (k >> 16));
                m += 3;
                j += 4;
            }
            return new String(decode, "US-ASCII").trim();
        } catch (Exception localException) {
        }
        return "";
    }

}
