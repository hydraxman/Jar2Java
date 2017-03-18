package com.bryansharp.jar2java.entity.refs;

/**
 * Created by bsp on 17/3/18.
 */
public class ClassRef {
    public static final int SIZE = 32;
    public int classIdx;
    public int accessFlags;
    public int superclassIdx;
    public int interfacesOff;
    public int sourceFileIdx;
    public int annotationsOff;
    public int classDataOff;
    public int staticValuesOff;

    @Override
    public String toString() {
        return "ClassRef{" +
                "classIdx=" + classIdx +
                ", accessFlags=" + accessFlags +
                ", superclassIdx=" + superclassIdx +
                ", interfacesOff=" + interfacesOff +
                ", sourceFileIdx=" + sourceFileIdx +
                ", annotationsOff=" + annotationsOff +
                ", classDataOff=" + classDataOff +
                ", staticValuesOff=" + staticValuesOff +
                '}';
    }
}
