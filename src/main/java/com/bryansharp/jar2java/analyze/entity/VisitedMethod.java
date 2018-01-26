package com.bryansharp.jar2java.analyze.entity;

import com.bryansharp.jar2java.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bushaopeng on 18/1/25.
 */
public class VisitedMethod {
    private int access;
    private VisitedClass ownerClass;
    private String name;
    private String description;
    private String signature;
    private String[] exceptions;
    private int maxStack;
    private int maxLocals;
    private boolean isInterfaceMethod;
    private List<VisitedMethod> invokeBy;
    private VisitedMethod implemented;
    private boolean extandsMethod;
    private VisitedMethod extanded;

    public VisitedMethod getImplemented() {
        return implemented;
    }

    public boolean isExtandsMethod() {
        return extandsMethod;
    }

    public VisitedMethod getExtanded() {
        return extanded;
    }

    public boolean isInterfaceMethod() {
        return isInterfaceMethod;
    }

    public void setInterfaceMethod(boolean interfaceMethod) {
        isInterfaceMethod = interfaceMethod;
    }

    public List<VisitedMethod> getInvokeBy() {
        return invokeBy;
    }

    public List<InvokedMethod> getInvokedMethods() {
        return invokedMethods;
    }

    public void setInvokedMethods(List<InvokedMethod> invokedMethods) {
        this.invokedMethods = invokedMethods;
    }

    private List<InvokedMethod> invokedMethods;

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public VisitedClass getOwnerClass() {
        return ownerClass;
    }

    public void setOwnerClass(VisitedClass ownerClass) {
        this.ownerClass = ownerClass;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String[] getExceptions() {
        return exceptions;
    }

    public void setExceptions(String[] exceptions) {
        this.exceptions = exceptions;
    }

    public int getMaxStack() {
        return maxStack;
    }

    public void setMaxStack(int maxStack) {
        this.maxStack = maxStack;
    }

    public int getMaxLocals() {
        return maxLocals;
    }

    public void setMaxLocals(int maxLocals) {
        this.maxLocals = maxLocals;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addInvokedMethod(InvokedMethod invokedMethod) {
        if (invokedMethods == null) {
            invokedMethods = new ArrayList<>();
        }
        invokedMethods.add(invokedMethod);
    }

    public String getKey() {
        return name + description;
    }

    public void addInvokedBy(VisitedMethod visitedMethod) {
        if (invokeBy == null) {
            invokeBy = new ArrayList<VisitedMethod>();
        }
        invokeBy.add(visitedMethod);
    }

    public String getAccessString() {
        return Utils.accCode2StringPrettry(access);
    }

    @Override
    public String toString() {
        return ownerClass.getName() + " :: " + getAccessString() + " " + getKey();
    }

    public void setImplemented(VisitedMethod implemented) {
        this.implemented = implemented;
    }

    public boolean isPublic() {
        return Utils.isPublic(access);
    }

    public boolean isPrivate() {
        return Utils.isPrivate(access);
    }

    public void setExtandsMethod(boolean extandsMethod) {
        this.extandsMethod = extandsMethod;
    }

    public void setExtanded(VisitedMethod extanded) {
        this.extanded = extanded;
    }
}
