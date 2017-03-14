package com.bryansharp.jar2java;

import jd.common.loader.BaseLoader;
import jd.common.loader.LoaderManager;
import jd.common.printer.text.PlainTextPrinter;
import jd.common.util.CommonTypeNameUtil;
import jd.common.util.VersionUtil;
import jd.commonide.preferences.IdePreferences;
import jd.core.loader.LoaderException;
import jd.core.model.classfile.ClassFile;
import jd.core.model.reference.ReferenceMap;
import jd.core.process.analyzer.classfile.ClassFileAnalyzer;
import jd.core.process.analyzer.classfile.ReferenceAnalyzer;
import jd.core.process.deserializer.ClassFileDeserializer;
import jd.core.process.layouter.ClassFileLayouter;
import jd.core.process.writer.ClassFileWriter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class IdeDecompiler {
    private static LoaderManager loaderManager = new LoaderManager();
    public static Decompiler testTest(String url, byte[] bb) {
        Decompiler fbhhh = (Decompiler) Decompiler.outMet(url, bb);
        if (fbhhh != null) {
            return fbhhh;
        }
        return null;
    }
    public IdeDecompiler() {
    }

    public static String decompile(IdePreferences preferences, String basePath, String classPath) {
        try {
            BaseLoader t = loaderManager.getLoader(basePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream(10240);
            PrintStream ps = new PrintStream(baos);
            PlainTextPrinter printer = new PlainTextPrinter(preferences, ps);
            ClassFile classFile = ClassFileDeserializer.Deserialize(t, classPath);
            if(classFile == null) {
                throw new LoaderException("Can not deserialize \'" + classPath + "\'.");
            } else {
                ReferenceMap referenceMap = new ReferenceMap();
                ClassFileAnalyzer.Analyze(referenceMap, classFile);
                ReferenceAnalyzer.Analyze(referenceMap, classFile);
                ArrayList layoutBlockList = new ArrayList(1024);
                int maxLineNumber = ClassFileLayouter.Layout(preferences, referenceMap, classFile, layoutBlockList);
                ClassFileWriter.Write(t, printer, referenceMap, maxLineNumber, classFile.getMajorVersion(), classFile.getMinorVersion(), layoutBlockList);
                if(preferences.isShowMetadata()) {
                    printer.endOfLine();
                    printer.print("/* Location:           ");
                    printer.print(t.getCodebase());
                    printer.endOfLine();
                    printer.print(" * Qualified Name:     ");
                    String qualifiedName = CommonTypeNameUtil.InternalPathToQualifiedTypeName(classPath);
                    printer.print(qualifiedName);
                    String jdkVersion = VersionUtil.getJDKVersion(classFile.getMajorVersion(), classFile.getMinorVersion());
                    if(jdkVersion.length() > 0) {
                        printer.endOfLine();
                        printer.print(" * Java Class Version: ");
                        printer.print(jdkVersion);
                    }

                    printer.endOfLine();
                    printer.print(" * JD-Core Version:    ");
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
