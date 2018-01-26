package com.bryansharp.jar2java.analyze.entity;

import java.util.Map;

/**
 * Created by bushaopeng on 18/1/25.
 */
public class InvokedMethod {
    private int invokedOpcode;
    private String owner;
    private String name;
    private String desc;

    public int getInvokedOpcode() {
        return invokedOpcode;
    }

    public void setInvokedOpcode(int invokedOpcode) {
        this.invokedOpcode = invokedOpcode;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public VisitedMethod findInMap(Map<String, VisitedClass> visitedClassMap) {
        VisitedClass visitedClass = visitedClassMap.get(owner);
        if (visitedClass == null) {
            return null;
        }
        Map<String, VisitedMethod> methods = visitedClass.getMethods();
        if (methods == null) {
            return null;
        }
        return methods.get(getKey());
    }

    private String getKey() {
        return name + desc;
    }
}
