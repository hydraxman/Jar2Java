package com.bryansharp.jar2java;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

/**
 * Created by bushaopeng on 17/9/14.
 */
public class AnalyzeClassVisitor implements ClassVisitor {
    public AnalyzeClassVisitor(String className) {

    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        Utils.logEach("visit", Utils.accCode2String(access), name, signature, superName, interfaces);
    }

    @Override
    public void visitSource(String source, String debug) {
        Utils.logEach("visitSource", source, debug);
    }

    @Override
    public void visitOuterClass(String owner, String name, String desc) {
        Utils.logEach("visitOuterClass", owner, name, desc);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return null;
    }

    @Override
    public void visitAttribute(Attribute attr) {

    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {

    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        Utils.logEach("visit", Utils.accCode2String(access), name, desc, signature, value);
        return new AnalyzeFieldVisitor();
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        return new AnalyzeMethodVisitor();
    }

    @Override
    public void visitEnd() {
        Utils.logEach("visitEnd");
    }
}
