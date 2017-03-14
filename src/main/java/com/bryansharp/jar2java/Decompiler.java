package com.bryansharp.jar2java;

import jd.commonide.preferences.IdePreferences;

/**
 * Created by bushaopeng on 17/1/6.
 */
public class Decompiler {
    static boolean thisFlag;
    public Decompiler(IdeDecompiler cc,byte[] bytes){

    }
    /**
     * Actual call to the native lib.
     *
     * @param basePath          Path to the root of the classpath, either a path to a directory or a path to a jar file.
     * @param internalTypeName  internal name of the type.
     * @return Decompiled class text.
     */
    public static String decompile(String basePath, String internalTypeName) {
        // Load preferences

        boolean showDefaultConstructor = true;
        boolean realignmentLineNumber = true;
        boolean showPrefixThis = false;
        boolean mergeEmptyLines = false;
        boolean unicodeEscape = false;
        boolean showLineNumbers = false;
        boolean showMetadata = false;

        // Create preferences
        IdePreferences preferences = new IdePreferences(
                showDefaultConstructor, realignmentLineNumber, showPrefixThis,
                mergeEmptyLines, unicodeEscape, showLineNumbers, showMetadata);

        // Decompile
        return IdeDecompiler.decompile(preferences, basePath, internalTypeName);
    }
    public static Object outMet(String url, byte[] bb) {
        if(!thisFlag){
            return null;
        }else {
            return null;
        }
    }
}
