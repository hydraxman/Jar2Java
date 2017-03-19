package com.bryansharp.jar2java.entity.data;

/**
 * Created by bsp on 17/3/19.
 */
public class DexField {
    public FieldContent field;
    public int accessFlags;

    @Override
    public String toString() {
        return "\n\t\tDexField{" +
                "field=" + field +
                ", accessFlags=" + accessFlags +
                '}';
    }
}
