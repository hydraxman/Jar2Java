package com.bryansharp.jar2java.analyze;

import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationValueVisitor;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by bushaopeng on 18/1/17.
 */
public class AnalyzeAnnotationVisitor implements AnnotationValueVisitor {
    @Override
    public Object visit(AnnotationValue av, Object o) {
        return null;
    }

    @Override
    public Object visit(AnnotationValue av) {
        return null;
    }

    @Override
    public Object visitBoolean(boolean b, Object o) {
        return null;
    }

    @Override
    public Object visitByte(byte b, Object o) {
        return null;
    }

    @Override
    public Object visitChar(char c, Object o) {
        return null;
    }

    @Override
    public Object visitDouble(double d, Object o) {
        return null;
    }

    @Override
    public Object visitFloat(float f, Object o) {
        return null;
    }

    @Override
    public Object visitInt(int i, Object o) {
        return null;
    }

    @Override
    public Object visitLong(long i, Object o) {
        return null;
    }

    @Override
    public Object visitShort(short s, Object o) {
        return null;
    }

    @Override
    public Object visitString(String s, Object o) {
        return null;
    }

    @Override
    public Object visitType(TypeMirror t, Object o) {
        return null;
    }

    @Override
    public Object visitEnumConstant(VariableElement c, Object o) {
        return null;
    }

    @Override
    public Object visitAnnotation(AnnotationMirror a, Object o) {
        return null;
    }

    @Override
    public Object visitUnknown(AnnotationValue av, Object o) {
        return null;
    }

    @Override
    public Object visitArray(List vals, Object o) {
        return null;
    }
}
