package com.bryansharp.jar2java.analyze;

import com.bryansharp.jar2java.Utils;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.FieldVisitor;

/**
 * Created by bushaopeng on 17/9/14.
 */
public class AnalyzeFieldVisitor implements FieldVisitor {

    @Override
    public AnnotationVisitor visitAnnotation(String s, boolean b) {
        Utils.logEach("visitAttribute", s, b);
        return null;
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        Utils.logEach("visitAttribute", attribute.type);
    }

    @Override
    public void visitEnd() {
        Utils.logEach("visitEnd");
    }
}
