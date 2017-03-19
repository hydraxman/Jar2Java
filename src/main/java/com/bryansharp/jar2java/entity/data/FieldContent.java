package com.bryansharp.jar2java.entity.data;

/**
 * Created by bsp on 17/3/18.
 */
public class FieldContent {
    public String className;
    public String fieldtype;
    public String name;

    @Override
    public String toString() {
        return "\nFieldContent{" +
                "className='" + className + '\'' +
                ", fieldtype='" + fieldtype + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
