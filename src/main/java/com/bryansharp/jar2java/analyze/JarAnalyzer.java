package com.bryansharp.jar2java.analyze;

import com.bryansharp.jar2java.Utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
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
//                    renameClassInJar(innerJar);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private File renameClassInJar(File jarFile, Map<String, String> renameMap) throws IOException {
        File outputJar = new File(jarFile.getParentFile(), "new-" + jarFile.getName());
        if (outputJar.exists()) {
            outputJar.delete();
        }
        JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(outputJar));

        JarFile file = new JarFile(jarFile);
        Enumeration<JarEntry> enumeration = file.entries();
        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = enumeration.nextElement();
            InputStream inputStream = file.getInputStream(jarEntry);

            String entryName = jarEntry.getName();
            String className;
            byte[] sourceClassBytes = IOUtils.toByteArray(inputStream);
            boolean entryNamePut = false;
            if (entryName.endsWith(".class")) {
                className = Utils.path2Classname(entryName);
                byte[] bytes = renameClass(className, sourceClassBytes, renameMap);
                if (renameMap.keySet().contains(className)) {
                    String newEntryName = getNewEntryName(entryName, renameMap.get(className));
                    ZipEntry zipEntry = new ZipEntry(newEntryName);
                    jarOutputStream.putNextEntry(zipEntry);
                } else {
                    ZipEntry zipEntry = new ZipEntry(entryName);
                    jarOutputStream.putNextEntry(zipEntry);
                }
                entryNamePut = true;
                jarOutputStream.write(bytes);
            }
            if (!entryNamePut) {
                ZipEntry zipEntry = new ZipEntry(entryName);
                jarOutputStream.putNextEntry(zipEntry);
            }
            jarOutputStream.closeEntry();
        }
        jarOutputStream.close();
        return outputJar;
    }

    private String getNewEntryName(String entryName, String newClassname) {
        return entryName.substring(0, entryName.lastIndexOf('/') + 1)
                + newClassname.substring(newClassname.lastIndexOf('.') + 1)
                + entryName.substring(entryName.lastIndexOf("."));
    }

    public File renameClassInJar(String jarFilePath, final Map<String, String> renameMap) {
        try {
            return renameClassInJar(new File(jarFilePath), renameMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] renameClass(String className, byte[] sourceClassBytes, final Map<String, String> renameMap) {

        Utils.log("className: " + className + "原大小：" + sourceClassBytes.length);
        final ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
//        ClassVisitor adapter = new AnalyzeClassVisitor(className, classWriter);

        final ClassLoader classLoader = getClass().getClassLoader();
        ClassVisitor adapter = (ClassVisitor) Proxy.newProxyInstance(classLoader, classWriter.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                boolean replaceInArgs = replaceInArgs(method, args, renameMap);
                final Object invoke = method.invoke(classWriter, args);
                if (replaceInArgs) {
                    logProxy(method, args, invoke, 0);
                }
                if (invoke != null) {
                    return Proxy.newProxyInstance(classLoader, invoke.getClass().getInterfaces(), new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy1, Method method1, Object[] args1) throws Throwable {
                            boolean replaceInArgs1 = replaceInArgs(method1, args1, renameMap);
                            Object ret = method1.invoke(invoke, args1);
//                            if (replaceInArgs1) {
//                                logProxy(method1, args1, ret, 1);
//                            }
                            return ret;
                        }
                    });
                }
                return null;
            }
        });

        ClassReader cr = new ClassReader(sourceClassBytes);
        //cr.accept(visitor, ClassReader.SKIP_DEBUG);
        cr.accept(adapter, 0);
        Utils.log("className: " + className + "新类大小：" + classWriter.toByteArray().length);
        return classWriter.toByteArray();
    }

    private boolean replaceInArgs(Method method, Object[] args, final Map<String, String> renameMap) {
        boolean replaced = false;
        Set<String> keySet = renameMap.keySet();
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                if (args[i] == null) {
                    continue;
                }
                for (String classname : keySet) {
                    String path = Utils.classname2Path(classname);
                    String newPath = Utils.classname2Path(renameMap.get(classname));
                    if (path.equals(args[i])) {
                        args[i] = newPath;
                        replaced = true;
                    } else {
                        if (args[i] instanceof String) {
                            String name = (String) args[i];
                            if (name.contains(path)) {
                                args[i] = name.replace(path, newPath);
                            }
                        }
                    }
                }
            }
        }
        return replaced;
    }

    private void logProxy(Method method, Object[] args, Object returnedVal, int indent) {
        StringBuilder builder = new StringBuilder();
        if (args != null && args.length > 0) {
            for (Object arg : args) {
                if (arg != null) {
                    builder.append(arg.toString());
                } else {
                    builder.append("null");
                }
                builder.append("\t");
            }
            builder.setLength(builder.length() - 1);
        }
        if (returnedVal != null) {
            builder.append(", 返回值：").append(returnedVal);
        } else {
            builder.append("，无返回值");
        }
        Utils.log((indent == 0 ? "" : "\t") + "调用：" + method.getName() + ", 参数: " + builder.toString());
    }

    public Map<String, String> getRenameMap(String jarFilePath) {
        Map<String, String> map = new HashMap<>();
        try {
            File jarFile = new File(jarFilePath);
            JarFile file = new JarFile(jarFile);
            Enumeration<JarEntry> enumeration = file.entries();
            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = enumeration.nextElement();
                String entryName = jarEntry.getName();
                String className;
                if (entryName.endsWith(".class")) {
                    className = Utils.path2Classname(entryName);
                    String simpleName = className.substring(className.lastIndexOf('.') + 1);
                    if (isProguardedName(simpleName)) {
                        map.put(className, getNewClassName(className, simpleName));
                    }
                }
            }
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

    private String getNewClassName(String className, String simpleName) {
        String hex = DigestUtils.md5Hex(className + simpleName);
        hex = hex.substring(hex.length() - 5);
        return className.substring(0, className.lastIndexOf('.') + 1) + simpleName.toUpperCase() + hex;
    }

    private boolean isProguardedName(String simpleName) {
        if (simpleName.matches("[a-z]{1,2}")) {
            return true;
        }
        return false;
    }
}
