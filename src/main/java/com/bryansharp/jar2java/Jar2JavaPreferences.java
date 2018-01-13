package com.bryansharp.jar2java;

import jd.common.preferences.CommonPreferences;

public class Jar2JavaPreferences extends CommonPreferences {
    protected boolean showMetadata;

    public Jar2JavaPreferences() {
        this.showMetadata = true;
    }

    public Jar2JavaPreferences(boolean showDefaultConstructor, boolean realignmentLineNumber, boolean showPrefixThis, boolean mergeEmptyLines, boolean unicodeEscape, boolean showLineNumbers, boolean showMetadata) {
        super(showDefaultConstructor, realignmentLineNumber, showPrefixThis, mergeEmptyLines, unicodeEscape, showLineNumbers);
        this.showMetadata = showMetadata;
    }

    public boolean isShowMetadata() {
        return this.showMetadata;
    }
}