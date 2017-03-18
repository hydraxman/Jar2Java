package com.bryansharp.jar2java.entity.refs;

/**
 * Created by bsp on 17/3/18.
 */
public class ProtoRef {
    public static final int SIZE = 12;
    public int shortyIdx;
    public int returnTypeIdx;
    public int parametersOff;

    @Override
    public String toString() {
        return "ProtoRef{" +
                "shortyIdx=" + shortyIdx +
                ", returnTypeIdx=" + returnTypeIdx +
                ", parametersOff=" + parametersOff +
                '}';
    }
}
