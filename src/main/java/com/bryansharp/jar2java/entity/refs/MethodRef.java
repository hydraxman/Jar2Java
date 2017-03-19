package com.bryansharp.jar2java.entity.refs;

/**
 * Created by bsp on 17/3/18.
 */
public class MethodRef {
    public static final int SIZE = 8;
    public int classIdx;
    public int protoIdx;
    public int nameIdx;

    @Override
    public String toString() {
        return "\nMethodRef{" +
                "classIdx=" + classIdx +
                ", protoIdx=" + protoIdx +
                ", nameIdx=" + nameIdx +
                '}';
    }
}
