package com.bryansharp.jar2java.analyze;

import com.bryansharp.jar2java.Main;
import com.bryansharp.jar2java.TextFileWritter;
import com.bryansharp.jar2java.analyze.entity.VisitedClass;

import org.apache.commons.lang3.StringUtils;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * Created by bushaopeng on 18/1/25.
 */
public class ClassConstantAnalyzer implements ClassAnalyzer {

    private String projName;

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    @Override
    public byte[] modifyClass(final String className, byte[] sourceClassBytes, Map<String, VisitedClass> visitedClassMap) {
//        Utils.log("className: " + className + ", 大小：" + sourceClassBytes.length);
        final ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        final ClassLoader classLoader = getClass().getClassLoader();
        final VisitedClass visitedClass = new VisitedClass();
        visitedClass.setName(className);
        visitedClassMap.put(className, visitedClass);
        ClassVisitor adapter = (ClassVisitor) Proxy.newProxyInstance(classLoader, classWriter.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
                // 进行modify 逻辑
                final Object parsedVal = handleClassVisits(visitedClass, method, args);
                final Object classVisitReturnVal = method.invoke(classWriter, args);
                if (classVisitReturnVal != null) {
                    return Proxy.newProxyInstance(classLoader, classVisitReturnVal.getClass().getInterfaces(), new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy1, Method method1, Object[] args1) throws Throwable {
                            if (classVisitReturnVal instanceof FieldVisitor) {
                                FieldVisitor fieldVisitor = (FieldVisitor) classVisitReturnVal;
                                handleFieldVisits(className, method1, args1, fieldVisitor);
                            } else if (classVisitReturnVal instanceof MethodVisitor) {
                                MethodVisitor methodVisitor = (MethodVisitor) classVisitReturnVal;
//                                VisitedMethod visitedMethod = (VisitedMethod) parsedVal;
                                handleMethodVisits(className, method1, args1, methodVisitor);
                            } else if (classVisitReturnVal instanceof AnnotationVisitor) {
                                AnnotationVisitor annotationVisitor = (AnnotationVisitor) classVisitReturnVal;
                            }
                            Object ret = method1.invoke(classVisitReturnVal, args1);
//                            logProxy(method1, args1, ret, 1);
                            return ret;
                        }
                    });
                }
                return null;
            }
        });
        ClassReader cr = new ClassReader(sourceClassBytes);
        cr.accept(adapter, 0);
        return classWriter.toByteArray();
    }

    private void handleMethodVisits(String className, Method method, Object[] args, MethodVisitor methodVisitor) {
        String name = method.getName();

        switch (name) {
            case "visitLdcInsn":
                if (args[0] instanceof String) {
                    String str = (String) args[0];
                    if (projName.contains("5.0.0.6") || projName.contains("5.0.0.8") || projName.contains("5.0.0.8") || projName.contains("5.1.0")) {
                        if (str.toLowerCase().contains("android")) {
                            return;
                        }
                        String decode = Main.decodeStringInAMobi(str);
                        if (StringUtils.isNotBlank(decode) && containsNoIllegal(decode) && str.length() > 2) {
                            TextFileWritter.getWritter(projName).println(className + "类中访问常量:" + str + "  , 解密后：" + decode);
                        }
                    } else {
                        if (str.length() > 1) {
                            TextFileWritter.getWritter(projName).println(className + "类中访问常量:" + str);
                        }
                    }
                }
                break;
        }
    }

    private boolean containsNoIllegal(String decode) {
        for (int i = 0; i < decode.length(); i++) {
            if (decode.charAt(i) >= 65533) {
                return false;
            }
        }
        return true;
    }

    private void handleFieldVisits(String className, Method method, Object[] args, FieldVisitor fieldVisitor) {

    }

    private Object handleClassVisits(VisitedClass visitedClass, Method method, Object[] args) {
        String name = method.getName();
        switch (name) {
//            case "visit":
//                visitedClass.setAccess((Integer) args[1]);
//                visitedClass.setSignature((String) args[3]);
//                visitedClass.setSuperClassName(Utils.path2Classname((String) args[4]));
//                String[] interfaces = (String[]) args[5];
//                if (interfaces != null) {
//                    for (String anInterface : interfaces) {
//                        visitedClass.addInterface(Utils.path2Classname(anInterface));
//                    }
//                }
//                break;
//            case "visitSource":
//                visitedClass.setSource((String) args[0]);
//                break;
//            case "visitMethod":
//                VisitedMethod visitedMethod = new VisitedMethod();
//                visitedMethod.setOwnerClass(visitedClass);
//                visitedMethod.setAccess((Integer) args[0]);
//                visitedMethod.setName((String) args[1]);
//                visitedMethod.setDescription((String) args[2]);
//                visitedMethod.setSignature((String) args[3]);
//                visitedMethod.setExceptions((String[]) args[4]);
//                visitedClass.addMethod(visitedMethod);
//                return visitedMethod;
            case "":
                break;
        }
        return null;
    }
}
