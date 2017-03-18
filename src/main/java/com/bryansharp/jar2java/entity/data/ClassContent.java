package com.bryansharp.jar2java.entity.data;

/**
 * Created by bsp on 17/3/18.
 */
public class ClassContent {
    public String className;
    public String superClassName;
    public String sourceFile;

    @Override
    public String toString() {
        return "ClassContent{" +
                "className='" + className + '\'' +
                ", superClassName='" + superClassName + '\'' +
                ", sourceFile='" + sourceFile + '\'' +
                '}';
    }
}
