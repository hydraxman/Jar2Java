package com.bryansharp.jar2java;

import javassist.*;
import javassist.bytecode.ConstPool;
import org.objectweb.asm.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

/**
 * Created by bushaopeng on 17/1/6.
 */
public class Main {
    private static File baseDir;

    private static boolean testAsmHook() {
        return IdeDecompiler.testTest("", null) != null;
    }


    public static void main(String[] args) {
        if (testAsmHook()) {
            return;
        }
        if (testReadJar()) {
            return;
        }
        if (args == null || args.length < 1) {
            LogUtils.log("please specify a jar file");
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
                    printJar(jarFile);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static boolean testReadJar() {
        try {
            initClassPathList(Arrays.asList(
                    "/Users/bushaopeng/Desktop/androidSDK/platforms/android-21/android.jar"
                    , "/Users/bushaopeng/IdeaProjects/Jar2Java/classes.jar"));
            CtClass ctClass = ClassPool.getDefault().get("com.facebook.ads.internal.e.f");
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(byteArrayOutputStream);
                ctClass.getClassFile().write(out);
                out.flush();
                byte[] bytes = byteArrayOutputStream.toByteArray();
                testWithAsm(new ByteArrayInputStream(bytes));
            } catch (IOException e) {
                e.printStackTrace();
            }
//            testWithJavaAssist(ctClass);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }

    private static void testWithJavaAssist(CtClass ctClass) throws NotFoundException {
        CtMethod[] declaredMethods = ctClass.getDeclaredMethods();
        for (CtMethod declaredMethod : declaredMethods) {
            if (declaredMethod.getLongName().contains("b(android")) {
//                printConstpool(declaredMethod);
            }
        }
    }

    private static void printConstpool(CtMethod declaredMethod) {
        ConstPool constPool = declaredMethod.getMethodInfo().getConstPool();
        for (int i = 0; i < constPool.getSize(); i++) {
            try {
                LogUtils.log(i + ":" + constPool.getUtf8Info(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
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
                stringToClassFile(name, result);
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


    public static void initClassPathList(List<String> classPathList) {
        try {
            for (String classDir : classPathList) {
                File cp = new File(classDir);
                ClassPath mClassPath = ClassPool.getDefault().insertClassPath(cp.getAbsolutePath().replaceAll("\\\\", "/"));
            }
        } catch (NotFoundException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 获取参数名列表辅助方法
     *
     * @param in
     * @return
     * @throws IOException
     */
    private static void testWithAsm(InputStream in) throws IOException {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassAdapter adapter = new MethodChangeClassAdapter(classWriter);
        ClassReader cr = new ClassReader(in);
        cr.accept(adapter, ClassReader.SKIP_DEBUG);
        byte[] bytes = classWriter.toByteArray();
        File file = new File("./test.class");
        FileOutputStream stream = new FileOutputStream(file);
        stream.write(bytes);
    }

    static class MethodChangeClassAdapter extends ClassAdapter implements Opcodes {

        public MethodChangeClassAdapter(final ClassVisitor cv) {
            super(cv);
        }

        @Override
        public void visit(
                int version,
                int access,
                String name,
                String signature,
                String superName,
                String[] interfaces) {
            if (cv != null) {
                cv.visit(version, access, name, signature, superName, interfaces);
            }
        }

        @Override
        public MethodVisitor visitMethod(int access, String name,
                                         String desc, String signature, String[] exceptions) {
            if ("e".equals(name)) {
                MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);//先得到原始的方法
                MethodVisitor newMethod = new AsmMethodVisit(mv); //访问需要修改的方法
                return newMethod;
            }
            if (cv != null) {
                return cv.visitMethod(access, name, desc, signature, exceptions);
            }
//
            return null;
        }
    }

    static class AsmMethodVisit extends MethodAdapter {

        private boolean visited = false;

        public AsmMethodVisit(MethodVisitor mv) {
            super(mv);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc) {
            LogUtils.logEach("visitMethodInsn", LogUtils.getOpName(opcode), owner, name, desc);
            super.visitMethodInsn(opcode, owner, name, desc);
            if (!visited) {
                visited = true;
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/bsp/test", "a", "()Ljava/lang/String");
            }
        }

        @Override
        public void visitAttribute(Attribute attribute) {
            LogUtils.logEach("visitAttribute", attribute);
            super.visitAttribute(attribute);
        }

        @Override
        public void visitEnd() {
            LogUtils.log("visitEnd");
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/bsp/test", "a", "()Ljava/lang/String");
            super.visitEnd();
        }

        @Override
        public void visitFieldInsn(int opcode, String owner, String name, String desc) {
            LogUtils.logEach("visitFieldInsn", LogUtils.getOpName(opcode), owner, name, desc);
            super.visitFieldInsn(opcode, owner, name, desc);
        }

        @Override
        public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
            LogUtils.logEach("visitFrame", type, nLocal, nStack);
            super.visitFrame(type, nLocal, local, nStack, stack);
        }

        @Override
        public void visitLabel(Label label) {
            LogUtils.logEach("visitLabel", label);
            super.visitLabel(label);
        }

        @Override
        public void visitLineNumber(int line, Label label) {
            LogUtils.logEach("visitLineNumber", line, label);
            super.visitLineNumber(line, label);
        }

        @Override
        public void visitIincInsn(int var, int increment) {
            LogUtils.logEach("visitIincInsn", var, increment);
            super.visitIincInsn(var, increment);
        }

        @Override
        public void visitIntInsn(int i, int i1) {
            LogUtils.logEach("visitIntInsn", i, i1);
            super.visitIntInsn(i, i1);
        }

        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
            LogUtils.logEach("visitMaxs", maxStack, maxLocals);
            super.visitMaxs(maxStack, maxLocals);
        }

        @Override
        public void visitVarInsn(int opcode, int var) {
            LogUtils.logEach("visitVarInsn", LogUtils.getOpName(opcode), var);
            super.visitVarInsn(opcode, var);
        }

        @Override
        public void visitJumpInsn(int opcode, Label label) {
            LogUtils.logEach("visitJumpInsn", LogUtils.getOpName(opcode), label);
            super.visitJumpInsn(opcode, label);
        }

        @Override
        public void visitLdcInsn(Object o) {
            LogUtils.logEach("visitLdcInsn", o);
            super.visitLdcInsn(o);
        }

        @Override
        public void visitLookupSwitchInsn(Label label, int[] ints, Label[] labels) {
            LogUtils.logEach("visitLookupSwitchInsn", label);
            super.visitLookupSwitchInsn(label, ints, labels);
        }

        @Override
        public void visitMultiANewArrayInsn(String s, int i) {
            LogUtils.logEach("visitMultiANewArrayInsn", s, i);
            super.visitMultiANewArrayInsn(s, i);
        }

        @Override
        public void visitTableSwitchInsn(int i, int i1, Label label, Label[] labels) {
            LogUtils.logEach("visitTableSwitchInsn", i, i1, label);
            super.visitTableSwitchInsn(i, i1, label, labels);
        }

        @Override
        public void visitTryCatchBlock(Label label, Label label1, Label label2, String s) {
            LogUtils.logEach("visitTryCatchBlock", label, label1, label2, s);
            super.visitTryCatchBlock(label, label1, label2, s);
        }

        @Override
        public void visitTypeInsn(int opcode, String s) {
            LogUtils.logEach("visitTypeInsn", LogUtils.getOpName(opcode), s);
            super.visitTypeInsn(opcode, s);
        }

        @Override
        public void visitCode() {
            //此方法在访问方法的头部时被访问到，仅被访问一次
            //此处可插入新的指令
            LogUtils.log("visitCode");
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/bsp/test", "a", "()Ljava/lang/String");
            super.visitCode();
        }

        @Override
        public void visitLocalVariable(String s, String s1, String s2, Label label, Label label1, int i) {
            LogUtils.logEach("visitLocalVariable", s, s1, s2, label, label1, i);
            super.visitLocalVariable(s, s1, s2, label, label1, i);
        }


        @Override
        public void visitInsn(int opcode) {
            //此方法可以获取方法中每一条指令的操作类型，被访问多次
            //如应在方法结尾处添加新指令，则应判断：
            LogUtils.logEach("visitInsn", LogUtils.getOpName(opcode));
            if (opcode == Opcodes.ARETURN) {
                // pushes the 'out' field (of type PrintStream) of the System class
                mv.visitFieldInsn(GETSTATIC,
                        "java/lang/System",
                        "out",
                        "Ljava/io/PrintStream;");
                // pushes the "Hello World!" String constant
                mv.visitLdcInsn("this is a modify method!");
                // invokes the 'println' method (defined in the PrintStream class)
                mv.visitMethodInsn(INVOKEVIRTUAL,
                        "java/io/PrintStream",
                        "println",
                        "(Ljava/lang/String;)V");
//                mv.visitInsn(RETURN);
            }
            super.visitInsn(opcode);
        }
    }

}
