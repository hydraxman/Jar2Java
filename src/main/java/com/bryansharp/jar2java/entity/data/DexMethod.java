package com.bryansharp.jar2java.entity.data;

/**
 * Created by bsp on 17/3/19.
 */
public class DexMethod {
    public MethodContent method;
    public int accessFlags;
    public int codeOff;
    public DexCode code = new DexCode();

    @Override
    public String toString() {
        return "\n\t\tDexMethod{" +
                "method=" + method +
                ", accessFlags=" + accessFlags +
                ", codeOff=" + codeOff +
                ", code=" + code +
                '}';
    }
}
