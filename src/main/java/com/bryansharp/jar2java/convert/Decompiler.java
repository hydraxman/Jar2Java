package com.bryansharp.jar2java.convert;


/**
 * Created by bushaopeng on 17/1/6.
 */
public class Decompiler {
    /**
     * Actual call to the native lib.
     *
     * @param basePath         Path to the root of the classpath, either a path to a directory or a path to a jar file.
     * @param internalTypeName internal name of the type.
     * @return Decompiled class text.
     */
    public static String decompile(String basePath, String internalTypeName) {
        // Load preferences
        boolean showDefaultConstructor = false;
        boolean realignmentLineNumber = true;
        boolean showPrefixThis = false;
        boolean mergeEmptyLines = true;
        boolean unicodeEscape = false;
        boolean showLineNumbers = false;
        boolean showMetadata = true;

        // Create preferences
        Jar2JavaPreferences preferences = new Jar2JavaPreferences(
                showDefaultConstructor, realignmentLineNumber, showPrefixThis,
                mergeEmptyLines, unicodeEscape, showLineNumbers, showMetadata);

        // Decompile
        return Jar2JavaDecompiler.decompile(preferences, basePath, internalTypeName);
    }
}
