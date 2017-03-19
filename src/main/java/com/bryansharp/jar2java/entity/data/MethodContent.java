package com.bryansharp.jar2java.entity.data;

/**
 * Created by bsp on 17/3/18.
 */
public class MethodContent {
    public String className;
    public String proto;
    public String name;

    @Override
    public String toString() {
        return "\nMethodContent{" +
                "className='" + className + '\'' +
                ", proto='" + proto + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
