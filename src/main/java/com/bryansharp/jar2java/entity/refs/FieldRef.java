package com.bryansharp.jar2java.entity.refs;

/**
 * Created by bsp on 17/3/18.
 */
public class FieldRef {
    public static final int SIZE = 8;
    public int classIdx;
    public int typeIdx;
    public int nameIdx;

    @Override
    public String toString() {
        return "\nFieldRef{" +
                "classIdx=" + classIdx +
                ", typeIdx=" + typeIdx +
                ", nameIdx=" + nameIdx +
                '}';
    }
}
