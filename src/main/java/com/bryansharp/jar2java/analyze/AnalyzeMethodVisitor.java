package com.bryansharp.jar2java.analyze;

import com.bryansharp.jar2java.Utils;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

/**
 * Created by bushaopeng on 17/9/14.
 */
public class AnalyzeMethodVisitor implements MethodVisitor {
    @Override
    public AnnotationVisitor visitAnnotationDefault() {
        return null;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return null;
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
        return null;
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        Utils.logEach("visitAttribute", attribute);
    }

    @Override
    public void visitCode() {
        Utils.logEach("visitCode");
    }

    @Override
    public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
        Utils.logEach("visitLabel", type);
    }

    @Override
    public void visitInsn(int opcode) {
        Utils.logEach("visitInsn", Utils.getOpName(opcode));
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        Utils.logEach("visitIntInsn", Utils.getOpName(opcode), operand);
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        Utils.logEach("visitVarInsn", Utils.getOpName(opcode), var);
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        Utils.logEach("visitTypeInsn", Utils.getOpName(opcode), type);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        Utils.logEach("visitFieldInsn", Utils.getOpName(opcode), owner, name, desc);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        Utils.logEach("visitMethodInsn", Utils.getOpName(opcode), owner, name, desc);
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        Utils.logEach("visitJumpInsn", Utils.getOpName(opcode), label);
    }

    @Override
    public void visitLabel(Label label) {
        Utils.logEach("visitLabel", label);
    }

    @Override
    public void visitLdcInsn(Object cst) {
        Utils.logEach("visitLdcInsn", cst);
    }

    @Override
    public void visitIincInsn(int var, int increment) {

    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels) {
        Utils.logEach("visitTryCatchBlock", min, max, dflt, labels);
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {

    }

    @Override
    public void visitMultiANewArrayInsn(String desc, int dims) {

    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        Utils.logEach("visitTryCatchBlock", start, end, handler, type);
    }

    @Override
    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        Utils.logEach("visitLocalVariable", name, desc, signature, start, end, index);
    }

    @Override
    public void visitLineNumber(int line, Label start) {
        Utils.logEach("visitTryCatchBlock", line, start);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        Utils.logEach("visitMaxs", maxStack, maxLocals);
    }

    @Override
    public void visitEnd() {
        Utils.logEach("visitEnd");
    }
}
