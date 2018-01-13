//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.bryansharp.jar2java;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import jd.common.loader.BaseLoader;
import jd.common.loader.LoaderManager;
import jd.common.util.CommonTypeNameUtil;
import jd.common.util.VersionUtil;
import jd.core.loader.LoaderException;
import jd.core.model.classfile.ClassFile;
import jd.core.model.layout.block.LayoutBlock;
import jd.core.model.reference.ReferenceMap;
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
            JavaSourceTextPrinter printer = new JavaSourceTextPrinter(preferences, ps);
            ClassFile classFile = ClassFileDeserializer.Deserialize(loader, classPath);
            if (classFile == null) {
                throw new LoaderException("Can not deserialize \'" + classPath + "\'.");
            } else {
                ReferenceMap referenceMap = new ReferenceMap();
                ClassFileAnalyzer.Analyze(referenceMap, classFile);
                ReferenceAnalyzer.Analyze(referenceMap, classFile);
                ArrayList<LayoutBlock> layoutBlockList = new ArrayList<>(1024);
                int maxLineNumber = ClassFileLayouter.Layout(preferences, referenceMap, classFile, layoutBlockList);
                ClassFileWriter.Write(loader, printer, referenceMap, maxLineNumber, classFile.getMajorVersion(), classFile.getMinorVersion(), layoutBlockList);
                if (preferences.isShowMetadata()) {
                    printer.endOfLine();
                    printer.print("/* Location:    ");
                    printer.print(loader.getCodebase());
                    printer.endOfLine();
                    printer.print(" * Qualified Name:     ");
                    String qualifiedName = CommonTypeNameUtil.InternalPathToQualifiedTypeName(classPath);
                    printer.print(qualifiedName);
                    String jdkVersion = VersionUtil.getJDKVersion(classFile.getMajorVersion(), classFile.getMinorVersion());
                    if (jdkVersion.length() > 0) {
                        printer.endOfLine();
                        printer.print(" * Java Class Version: ");
                        printer.print(jdkVersion);
                    }

                    printer.endOfLine();
                    printer.print(" * By Jar2Java");
                    printer.endOfLine();
                    printer.print(" * Using JD-Core Version:    ");
                    printer.print("0.7.1");
                    printer.endOfLine();
                    printer.print(" */");
                }

                ps.close();
                return new String(baos.toByteArray());
            }
        } catch (Throwable var13) {
            return null;
        }
    }
}
