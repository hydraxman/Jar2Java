package com.bryansharp.jar2java.analyze.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bushaopeng on 18/1/25.
 */
public class VisitedClass {
    private int access;
    private String signature;
    private String name;
    private String source;
    private List<String> interfaces;
    private List<VisitedClass> interfacesObj;
    private List<VisitedClass> sons;
    private List<VisitedClass> implementations;
    private String superClassName;
    private VisitedClass superClass;
    private Map<String, VisitedMethod> methods;

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public List<String> getInterfaces() {
        return interfaces;
    }

    public List<VisitedClass> getInterfacesObj() {
        return interfacesObj;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSuperClassName() {
        return superClassName;
    }

    public void setSuperClassName(String superClassName) {
        this.superClassName = superClassName;
    }

    public VisitedClass getSuperClass() {
        return superClass;
    }

    public void setSuperClass(VisitedClass superClass) {
        this.superClass = superClass;
    }

    public Map<String, VisitedMethod> getMethods() {
        return methods;
    }

    public void setMethods(Map<String, VisitedMethod> methods) {
        this.methods = methods;
    }

    public void addMethod(VisitedMethod visitedMethod) {
        if (this.methods == null) {
            this.methods = new HashMap<>();
        }
        this.methods.put(visitedMethod.getKey(), visitedMethod);
    }

    public void addInterface(String interfaceName) {
        if (interfaces == null) {
            interfaces = new ArrayList<>();
        }
        interfaces.add(interfaceName);
    }

    public void addSon(VisitedClass visitedClass) {
        if (sons == null) {
            sons = new ArrayList<>();
        }
        sons.add(visitedClass);
    }

    public void addInterfaceObj(VisitedClass interfaceClass) {
        if (interfacesObj == null) {
            interfacesObj = new ArrayList<>();
        }
        interfacesObj.add(interfaceClass);
    }

    public void addImplementation(VisitedClass visitedClass) {
        if (implementations == null) {
            implementations = new ArrayList<>();
        }
        implementations.add(visitedClass);
    }
}
