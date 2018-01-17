//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.bryansharp.jar2java.convert;

import com.bryansharp.jar2java.Utils;
import com.bryansharp.jar2java.convert.decompiler.GuiPreferences;
import com.bryansharp.jar2java.convert.decompiler.PlainTextPrinter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;

import jd.common.loader.BaseLoader;
import jd.common.loader.LoaderManager;
import jd.common.util.CommonTypeNameUtil;
import jd.common.util.VersionUtil;
import jd.core.loader.LoaderException;
import jd.core.model.classfile.ClassFile;
import jd.core.model.layout.block.LayoutBlock;
import jd.core.model.reference.ReferenceMap;
import jd.core.printer.Printer;
import jd.core.process.analyzer.classfile.ClassFileAnalyzer;
import jd.core.process.analyzer.classfile.ReferenceAnalyzer;
import jd.core.process.deserializer.ClassFileDeserializer;
import jd.core.process.layouter.ClassFileLayouter;
import jd.core.process.writer.ClassFileWriter;

public class Jar2JavaDecompiler {
    private static LoaderManager loaderManager = new LoaderManager();

    public static String decompile(Jar2JavaPreferences preferences, String basePath, String classPath) {
        try {
            BaseLoader loader = loaderManager.getLoader(basePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024 * 10);
            PrintStream ps = new PrintStream(baos);

            ClassFile classFile = ClassFileDeserializer.Deserialize(loader, classPath);
            if (classFile == null) {
                throw new LoaderException("Can not deserialize \'" + classPath + "\'.");
            } else {
                ReferenceMap referenceMap = new ReferenceMap();
                ClassFileAnalyzer.Analyze(referenceMap, classFile);
                ReferenceAnalyzer.Analyze(referenceMap, classFile);

                String className = classFile.getThisClassName().replace('/', '.');

                Utils.log("decompile classname is " + className);

                ArrayList<LayoutBlock> layoutBlockList = new ArrayList<>(1024);
                int maxLineNumber = ClassFileLayouter.Layout(preferences, referenceMap, classFile, layoutBlockList);

                Printer printerProxy = getPrinter(preferences, ps, className);

                ClassFileWriter.Write(loader, printerProxy, referenceMap, maxLineNumber, classFile.getMajorVersion(), classFile.getMinorVersion(), layoutBlockList);
                if (preferences.isShowMetadata()) {
                    printerProxy.endOfLine();
                    printerProxy.print("/* Location:    ");
                    printerProxy.print(loader.getCodebase());
                    printerProxy.endOfLine();
                    printerProxy.print(" * Qualified Name:     ");
                    String qualifiedName = CommonTypeNameUtil.InternalPathToQualifiedTypeName(classPath);
                    printerProxy.print(qualifiedName);
                    String jdkVersion = VersionUtil.getJDKVersion(classFile.getMajorVersion(), classFile.getMinorVersion());
                    if (jdkVersion.length() > 0) {
                        printerProxy.endOfLine();
                        printerProxy.print(" * Java Class Version: ");
                        printerProxy.print(jdkVersion);
                    }

                    printerProxy.endOfLine();
                    printerProxy.print(" * By Jar2Java");
                    printerProxy.endOfLine();
                    printerProxy.print(" * Using JD-Core Version:    ");
                    printerProxy.print("0.7.1");
                    printerProxy.endOfLine();
                    printerProxy.print(" */");
                }

                ps.close();
                return new String(baos.toByteArray());
            }
        } catch (Throwable var13) {
            return null;
        }
    }

    private static Printer getPrinter(Jar2JavaPreferences preferences, PrintStream ps, String className) {
        final Printer printer = getInnerPrinter(preferences, ps);
        if (!"com.mobvista.msdk.a.a".equals(className)) {
            return printer;
        }
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                StringBuilder builder = new StringBuilder();
                if (args != null && args.length > 0) {
                    for (Object arg : args) {
                        if (arg != null) {
                            builder.append(arg.toString());
                        } else {
                            builder.append("null");
                        }
                        builder.append("\t");
                    }
                    builder.setLength(builder.length() - 1);
                }
                Utils.log("调用：" + method.getName() + ": " + builder.toString());
                return method.invoke(printer, args);
            }
        };
        return (Printer) Proxy.newProxyInstance(Jar2JavaDecompiler.class.getClassLoader(), printer.getClass().getInterfaces(), handler);
    }

    private static Printer getInnerPrinter(Jar2JavaPreferences preferences, PrintStream ps) {
//        PlainTextPrinter plainTextPrinter = new PlainTextPrinter();
//        plainTextPrinter.setPrintStream(ps);
//        GuiPreferences guiPreferences = new GuiPreferences();
//        guiPreferences.setShowLineNumbers(preferences.isShowLineNumbers());
//        plainTextPrinter.setPreferences(guiPreferences);
//        return plainTextPrinter;
        return new JavaSourceTextPrinter(preferences, ps);
    }
}
