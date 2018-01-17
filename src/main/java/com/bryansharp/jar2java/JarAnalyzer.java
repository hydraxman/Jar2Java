package com.bryansharp.jar2java;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by bushaopeng on 17/9/14.
 */
public class JarAnalyzer {
    public static File unzipEntryToTemp(ZipEntry element, ZipFile zipFile) throws IOException {
        InputStream stream = zipFile.getInputStream(element);
        byte[] array = IOUtils.toByteArray(stream);
        String hex = DigestUtils.md5Hex(element.getName());
        final File tempDir = new File("/Users/bushaopeng/IdeaProjects/Jar2Java/build");
        File targetFile = new File(tempDir, hex + ".jar");
        if (targetFile.exists()) {
            targetFile.delete();
        }
        new FileOutputStream(targetFile).write(array);
        return targetFile;
    }

    public boolean analyzeAar(String path) {
        try {
            ZipFile zipFile = new ZipFile(new File(path));
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry element = entries.nextElement();
                String name = element.getName();
                if (name.endsWith(".jar")) {
                    File innerJar = unzipEntryToTemp(element, zipFile);
                    analyzeJar(innerJar);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private void analyzeJar(File jarFile) throws IOException {
        JarFile file = new JarFile(jarFile);
        Enumeration<JarEntry> enumeration = file.entries();
        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = enumeration.nextElement();
            InputStream inputStream = file.getInputStream(jarEntry);

            String entryName = jarEntry.getName();
            String className;
            byte[] sourceClassBytes = IOUtils.toByteArray(inputStream);
            if (entryName.endsWith(".class")) {
                className = Utils.path2Classname(entryName);
                analyzeClass(className, sourceClassBytes);
            }
        }
    }

    private void analyzeClass(String className, byte[] sourceClassBytes) {
//        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassVisitor adapter = new AnalyzeClassVisitor(className);
        ClassReader cr = new ClassReader(sourceClassBytes);
        //cr.accept(visitor, ClassReader.SKIP_DEBUG);
        cr.accept(adapter, 0);
    }
}
