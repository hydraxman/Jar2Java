package com.bryansharp.jar2java.analyze;

import com.bryansharp.jar2java.TextFileWritter;
import com.bryansharp.jar2java.Utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.bryansharp.jar2java.Utils.logProxy;
import static com.bryansharp.jar2java.Utils.unzipEntryToTemp;

/**
 * Created by bushaopeng on 18/1/25.
 */
public class JarModifier {

    public boolean modifyAar(String path) {
        try {
            File file = new File(path);
            ZipFile zipFile = new ZipFile(file);

            File outputAar = new File(file.getParentFile(), "new-" + file.getName());
            if (outputAar.exists()) {
                outputAar.delete();
            }
            JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(outputAar));

            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry element = entries.nextElement();

                InputStream inputStream = zipFile.getInputStream(element);

                String entryName = element.getName();
                ZipEntry zipEntry = new ZipEntry(entryName);
                jarOutputStream.putNextEntry(zipEntry);
                if (!element.isDirectory()) {
                    if (entryName.endsWith(".jar")) {
                        File innerJar = unzipEntryToTemp(element, zipFile);
//                        File newJar = renameClassInJar(innerJar);
                        File newJar = modifyJar(innerJar);
                        jarOutputStream.write(IOUtils.toByteArray(new FileInputStream(newJar)));
                    } else {
                        byte[] source = IOUtils.toByteArray(inputStream);
                        jarOutputStream.write(source);
                    }
                }
                jarOutputStream.closeEntry();
            }
            jarOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private File renameClassInJar(File jarFile) {
        try {
            Map<String, String> renameMap = getRenameMap(jarFile.getAbsolutePath());
            return renameClassInJar(jarFile, renameMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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

    public File modifyJar(File jarFile) throws IOException {
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
                byte[] bytes = modifyClass(className, sourceClassBytes);
                ZipEntry zipEntry = new ZipEntry(entryName);
                jarOutputStream.putNextEntry(zipEntry);
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

    private byte[] renameClass(final String className, byte[] sourceClassBytes, final Map<String, String> renameMap) {

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
                            if (replaceInArgs1) {
                                logProxy(method1, args1, ret, 1);
                            }
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

    private byte[] modifyClass(final String className, byte[] sourceClassBytes) {
        Utils.log("className: " + className + "原大小：" + sourceClassBytes.length);
        final ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        final ClassLoader classLoader = getClass().getClassLoader();
        ClassVisitor adapter = (ClassVisitor) Proxy.newProxyInstance(classLoader, classWriter.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
                // 进行modify 逻辑
                final int shouldVisit = modifyOneClass(className, method, args);
                final Object invoke = method.invoke(classWriter, args);
                if (invoke != null) {
                    return Proxy.newProxyInstance(classLoader, invoke.getClass().getInterfaces(), new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy1, Method method1, Object[] args1) throws Throwable {
                            modifyInner(className, method1, args1, shouldVisit, invoke);
                            Object ret = method1.invoke(invoke, args1);
//                            logProxy(method1, args1, ret, 1);
                            return ret;
                        }
                    });
                }
                return null;
            }
        });
        ClassReader cr = new ClassReader(sourceClassBytes);
        cr.accept(adapter, 0);
//        Utils.log("className: " + className + "新类大小：" + classWriter.toByteArray().length);
        return classWriter.toByteArray();
    }

    private int modifyOneClass(String className, Method method, Object[] args) {
        if ("com.mobi.sdk.procedure".equals(className)) {
            if ("visitMethod".equals(method.getName())) {
                if ("<clinit>".equals(args[1])) {
                    logProxy(method, args, null, 0);
                    return 1;
                }
                if (Utils.isPublicStatic((Integer) args[0])) {
                    logProxy(method, args, null, 0);
                    return 2;
                }
            }
        }
        if ("com.mobi.sdk.double".equals(className)) {
            if ("visitMethod".equals(method.getName())) {
                if ("do".equals(args[1])) {
                    if (((String) args[2]).equals("([B)Ljava/lang/String;")) {
                        logProxy(method, args, null, 0);
                        return 1;
                    }
                }
            }
        }
        return 0;
    }

    private int readCounter = 0;
    private int vis = 0;

    private void modifyInner(String className, Method method, Object[] args, int shouldVisit, Object invoke) {
        String methodName = method.getName();
        if (readCounter > 0) {
            if (!"visitLineNumber".equals(methodName)) {
                readCounter--;
            }
            logProxy(method, args, null, 1);
        }
        if ("com.mobi.sdk.am".equals(className) && "visitMethodInsn".equals(methodName)) {
            if ("getContentResolver".equals(args[2]) && vis == 0) {
                vis++;
                MethodVisitor methodVisitor = (MethodVisitor) invoke;
                methodVisitor.visitVarInsn(Opcodes.ALOAD, 2);
                methodVisitor.visitVarInsn(Opcodes.ALOAD, 4);
                methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, Utils.className2Path("com.altamob.CallbackApi"),
                        "callback", "(Ljava/lang/Object;Ljava/lang/Object;)V");

            }
        }
//        if (searchInArgs(args, "Landroid/content/ContentResolver;")) {
//            Utils.log("searchInArgs -----> className: " + className);
//            logProxy(method, args, null, 1);
//            readCounter = 8;
//        }
        if ("com.mobi.sdk.procedure".equals(className)) {
            if (shouldVisit == 1) {
                logProxy(method, args, null, 1);
                if ("visitInsn".equals(methodName)) {
                    Integer opCode = (Integer) args[0];
                    if (opCode == Opcodes.ICONST_0) {
                        args[0] = Opcodes.ICONST_1;
                        logProxy(method, args, null, 1);
                    }
                }
            } else if (shouldVisit == 2) {
                logProxy(method, args, null, 1);
                if ("visitCode".equals(methodName)) {
                    if (invoke instanceof MethodVisitor) {
//                        MethodVisitor methodVisitor = (MethodVisitor) invoke;
//                        methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
//                        methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
//                        methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, Utils.className2Path("com.altamob.CallbackApi"),
//                                "callback", "(Ljava/lang/Object;Ljava/lang/Object;)V");
                    }
                }
                if ("visitJumpInsn".equals(methodName)) {
                    Integer opCode = (Integer) args[0];
                    if (opCode == Opcodes.IFEQ) {
                        if (invoke instanceof MethodVisitor) {
                            MethodVisitor methodVisitor = (MethodVisitor) invoke;
                            methodVisitor.visitInsn(Opcodes.POP);

                            methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
                            methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
                            methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, Utils.className2Path("com.altamob.CallbackApi"),
                                    "callback", "(Ljava/lang/Object;Ljava/lang/Object;)V");

                            methodVisitor.visitInsn(Opcodes.ICONST_1);
                        }
                    }
                }
            }
        } else if ("com.mobi.sdk.finally".equals(className)) {
            if (shouldVisit == 1) {
                if ("visitInsn".equals(methodName)) {
                    Integer opCode = (Integer) args[0];
                    if (opCode == Opcodes.ARETURN && !visited) {
                        visited = true;
                        MethodVisitor methodVisitor = (MethodVisitor) invoke;
                        methodVisitor.visitVarInsn(Opcodes.ASTORE, 6);
                        methodVisitor.visitVarInsn(Opcodes.ALOAD, 6);
                        methodVisitor.visitVarInsn(Opcodes.ALOAD, 6);
                        methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, Utils.className2Path("com.altamob.CallbackApi"),
                                "callback", "(Ljava/lang/Object;Ljava/lang/Object;)V");
                        methodVisitor.visitVarInsn(Opcodes.ALOAD, 6);
                        Utils.log("------->");
                    }
                }
                logProxy(method, args, null, 1);
            }
        }
    }

    private boolean searchInArgs(Object[] args, String searchIn) {
        if (args == null) {
            return false;
        }
        for (Object arg : args) {
            if (arg instanceof String) {
                String stringArg = (String) arg;
                if (stringArg.contains(searchIn)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean visited = false;

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
                            String signiture = (String) args[i];
                            if (signiture.contains(path)) {
                                StringBuilder builder = new StringBuilder();
                                boolean seeNext = false;
                                Object appendObj = null;
                                for (int charPos = 0; charPos < signiture.length(); charPos++) {
                                    if (seeNext) {
                                        seeNext = false;
                                        int endIndex = signiture.indexOf(';', charPos);
                                        if (endIndex < 0) {
                                            // fixme double L logic
                                            appendObj = signiture.charAt(charPos);
                                        } else {
                                            String maybeClassRef = signiture.substring(charPos, endIndex);
                                            charPos = endIndex - 1;
                                            if (!path.equals(maybeClassRef)) {
                                                appendObj = maybeClassRef;
                                            } else {
                                                appendObj = newPath;
                                            }
                                        }
                                    } else {
                                        char cCar = signiture.charAt(charPos);
                                        if (cCar == 'L') {
                                            seeNext = true;
                                        }
                                        appendObj = cCar;
                                    }
                                    builder.append(appendObj);
                                }
                                String newSignture = builder.toString();
                                replaced = !newSignture.equals(args[i]);
                                args[i] = newSignture;
                            }
                        }
                    }
                }
            }
        }
        return replaced;
    }


    public Map<String, String> getRenameMap(String jarFilePath) {
        Map<String, String> map = new HashMap<>();
        try {
            File jarFile = new File(jarFilePath);
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
                    String simpleName = className.substring(className.lastIndexOf('.') + 1);
                    if (Utils.isProguardedName(simpleName)) {
                        map.put(className, getNewClassName(className, simpleName, sourceClassBytes));
                    }
                }
            }
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utils.log("Rename Map::--->");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            Utils.log(entry.getKey() + "->" + entry.getValue());
        }
        return map;
    }

    private String getNewClassName(String className, String simpleName, byte[] sourceClassBytes) {
        String prefix = className.substring(0, className.lastIndexOf('.') + 1);

        Utils.log("getNewClassName className: " + className + "原大小：" + sourceClassBytes.length);
        AnalyzeClassVisitor adapter = new AnalyzeClassVisitor(className);
        ClassReader cr = new ClassReader(sourceClassBytes);
        //cr.accept(visitor, ClassReader.SKIP_DEBUG);
        cr.accept(adapter, 0);
        String source = adapter.getSource();
        if (StringUtils.isNotBlank(source)) {
            return prefix + source.substring(0, source.indexOf('.')) + new Random().nextInt(300);
        }
        String hex = DigestUtils.md5Hex(className + simpleName);
        hex = hex.substring(hex.length() - 5);
        return prefix + simpleName.toUpperCase() + hex;
    }

    public Map<String, String> getReproguardMapping(String jarPath) {
        Map<String, String> renameMap = new HashMap<>();
        try {
            JarFile file = new JarFile(new File(jarPath));
            Enumeration<JarEntry> enumeration = file.entries();
            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = enumeration.nextElement();
                InputStream inputStream = file.getInputStream(jarEntry);
                String entryName = jarEntry.getName();
                String className;
                byte[] sourceClassBytes = IOUtils.toByteArray(inputStream);
                if (entryName.endsWith(".class")) {
                    className = Utils.path2Classname(entryName);
                    String newClassname = getReproguardClassname(className);
                    TextFileWritter.getDefaultWritter().println(className + (newClassname != null ? " -> " + newClassname : ""));
//                    analyzeClassNames(className, sourceClassBytes);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        TextFileWritter.getDefaultWritter().close();
        return renameMap;
    }

    private String getReproguardClassname(String className) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean changed = false;
        String[] split = className.split("\\.");
        int count = 0;
        for (String s : split) {
            if (count == split.length - 1) {
                if (Utils.isProguardedName(s)) {
                    changed = true;
                    String suffix = DigestUtils.md5Hex(className).substring(0, 4);
                    stringBuilder.append(s.toUpperCase()).append(suffix);
                } else {
                    stringBuilder.append(".");
                }
            } else {
                if (Utils.isKeyWord(s)) {
                    changed = true;
                    String suffix = DigestUtils.md5Hex(className).substring(0, 4);
                    stringBuilder.append(s).append(suffix);
                } else {
                    stringBuilder.append(s);
                }
                stringBuilder.append(".");
            }
            count++;
        }
        if (changed) {
            return stringBuilder.toString();
        }
        return null;
    }
}
