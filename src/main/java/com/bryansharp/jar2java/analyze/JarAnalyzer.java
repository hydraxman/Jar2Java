package com.bryansharp.jar2java.analyze;

import com.bryansharp.jar2java.Main;
import com.bryansharp.jar2java.Utils;
import com.bryansharp.jar2java.analyze.entity.InvokedMethod;
import com.bryansharp.jar2java.analyze.entity.VisitedClass;
import com.bryansharp.jar2java.analyze.entity.VisitedMethod;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.bryansharp.jar2java.Utils.unzipEntryToTemp;

/**
 * Created by bushaopeng on 17/9/14.
 */
public class JarAnalyzer {

    public boolean analyzeAar(String path) {
        try {
            File file = new File(path);
            ZipFile zipFile = new ZipFile(file);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry element = entries.nextElement();
                String entryName = element.getName();
                if (!element.isDirectory()) {
                    if (entryName.endsWith(".jar")) {
                        File innerJar = unzipEntryToTemp(element, zipFile);
                        analyzeJar(innerJar);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    public boolean extractSource(String path) {
        try {
            File file = new File(path);
            ZipFile zipFile = new ZipFile(file);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry element = entries.nextElement();
                String entryName = element.getName();
                if (!element.isDirectory()) {
                    if (entryName.endsWith(".jar")) {
                        File innerJar = unzipEntryToTemp(element, zipFile);
                        Main.initBuildPath();
                        Main.turnJarIntoJava(innerJar.getAbsolutePath());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    public Map<String, VisitedClass> analyzeJar(String path) {
        try {
            return analyzeJar(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map<String, VisitedClass> analyzeJar(File jarFile) throws IOException {
        Map<String, VisitedClass> visitedClasses = new HashMap<>();
        JarFile file = new JarFile(jarFile);
        Enumeration<JarEntry> enumeration = file.entries();
        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = enumeration.nextElement();
            InputStream inputStream = file.getInputStream(jarEntry);
            String entryName = jarEntry.getName();
            String className;
            byte[] sourceClassBytes = IOUtils.toByteArray(inputStream);
            if (entryName.endsWith(".class")) {
                className = Utils.path2Classname(entryName);
                ClassMethodAnalyzer analyzer = new ClassMethodAnalyzer();
                analyzer.modifyClass(className, sourceClassBytes, visitedClasses);
            }
        }

        analyzeClassRelation(visitedClasses);
        analyzeInvoke(visitedClasses);

        String className = "com.google.android.finsky.be.f";

        listAllMethod(className, visitedClasses);

        listInvokeTree(className, "a(Lcom/google/android/finsky/be/c;)V", visitedClasses);

        return visitedClasses;
    }

    private void analyzeClassRelation(Map<String, VisitedClass> visitedClasses) {
        for (Map.Entry<String, VisitedClass> entry : visitedClasses.entrySet()) {
            VisitedClass visitedClass = entry.getValue();
            String superClassName = visitedClass.getSuperClassName();
            List<String> interfaces = visitedClass.getInterfaces();
            if (superClassName != null) {
                VisitedClass superClass = visitedClasses.get(superClassName);
                if (superClass != null) {
                    visitedClass.setSuperClass(superClass);
                    superClass.addSon(visitedClass);
                    Map<String, VisitedMethod> methods = visitedClass.getMethods();
                    if (methods != null) {
                        for (Map.Entry<String, VisitedMethod> methodEntry : methods.entrySet()) {
                            VisitedMethod visitedMethod = methodEntry.getValue();
                            if (!visitedMethod.isPrivate()) {
                                Map<String, VisitedMethod> methods1 = superClass.getMethods();
                                if (methods1 != null) {
                                    VisitedMethod visitedMethod1 = methods1.get(visitedMethod.getKey());
                                    if (visitedMethod1 != null) {
                                        visitedMethod.setExtandsMethod(true);
                                        visitedMethod.setExtanded(visitedMethod1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (interfaces != null) {
                for (String anInterface : interfaces) {
                    if (anInterface == null) {
                        continue;
                    }
                    VisitedClass interfaceClass = visitedClasses.get(anInterface);
                    if (interfaceClass == null) {
                        continue;
                    }
                    visitedClass.addInterfaceObj(interfaceClass);
                    interfaceClass.addImplementation(visitedClass);
                    Map<String, VisitedMethod> methods = visitedClass.getMethods();
                    if (methods != null) {
                        for (Map.Entry<String, VisitedMethod> methodEntry : methods.entrySet()) {
                            VisitedMethod visitedMethod = methodEntry.getValue();
                            if (visitedMethod.isPublic()) {
                                Map<String, VisitedMethod> methods1 = interfaceClass.getMethods();
                                if (methods1 != null) {
                                    VisitedMethod visitedMethod1 = methods1.get(visitedMethod.getKey());
                                    if (visitedMethod1 != null) {
                                        visitedMethod.setInterfaceMethod(true);
                                        visitedMethod.setImplemented(visitedMethod1);
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    private void listAllMethod(String className, Map<String, VisitedClass> visitedClasses) {
        Utils.log(className + " methods:");
        VisitedClass visitedClass = visitedClasses.get(className);
        for (Map.Entry<String, VisitedMethod> entry : visitedClass.getMethods().entrySet()) {
            VisitedMethod visitedMethod = entry.getValue();
            Utils.log("--> " + visitedMethod.getKey());
        }
    }

    private void listInvokeTree(String className, String methodKey, Map<String, VisitedClass> visitedClasses) {
        VisitedClass visitedClass = visitedClasses.get(className);
        if (visitedClass == null) {
            return;
        }
        VisitedMethod method = visitedClass.getMethods().get(methodKey);
        if (method == null) {
            return;
        }
        Utils.log("invoke Tree -------------------\n");
        printInvoke(method, 1);
    }

    private void printInvoke(VisitedMethod method, int count) {
        List<VisitedMethod> invokeBy = method.getInvokeBy();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            builder.append("\t");
        }
        String indent = builder.toString();
        if (invokeBy == null) {
            Utils.log(indent + method + " has no invoke by\n");
            return;
        }
        int newCount = count + 1;
        for (VisitedMethod visitedMethod : invokeBy) {
            Utils.log(indent + method + " -> invoke by ->" + visitedMethod);
            printInvoke(visitedMethod, newCount);
            if (visitedMethod.isInterfaceMethod()) {
                printInvoke(visitedMethod.getImplemented(), newCount);
            }
        }
    }

    private void analyzeInvoke(Map<String, VisitedClass> visitedClasses) {
        for (Map.Entry<String, VisitedClass> entry : visitedClasses.entrySet()) {
            VisitedClass visitedClass = entry.getValue();
            Map<String, VisitedMethod> methods = visitedClass.getMethods();
            if (methods == null) {
                continue;
            }
            for (Map.Entry<String, VisitedMethod> methodEntry : methods.entrySet()) {
                VisitedMethod visitedMethod = methodEntry.getValue();
                List<InvokedMethod> invokedMethods = visitedMethod.getInvokedMethods();
                if (invokedMethods == null) {
                    continue;
                }
                for (InvokedMethod invokedMethod : invokedMethods) {
                    VisitedMethod invokedMethodInMap = invokedMethod.findInMap(visitedClasses);
                    if (invokedMethodInMap != null) {
                        invokedMethodInMap.addInvokedBy(visitedMethod);
                    }
                }
            }
        }

    }

}
