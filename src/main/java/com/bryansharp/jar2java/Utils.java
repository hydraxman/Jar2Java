package com.bryansharp.jar2java;

import com.bryansharp.jar2java.entity.base.DvmOpcode;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bushaopeng on 17/1/24.
 */
public class Utils {
    static HashMap<Integer, String> opCodeMap = new HashMap<>();
    static HashMap<String, String> patternFormMap = new HashMap<>();
    static HashMap<Integer, DvmOpcode> dvmOpCodeMap = new HashMap<>();

    public static Map<String, String> getPatternFormMap() {
        if (patternFormMap.size() == 0) {
            patternFormMap.put("00x", "N/A");
            patternFormMap.put("10x", "ØØ|op");
            patternFormMap.put("12x", "B|A|op");
            patternFormMap.put("11n", "B|A|op");
            patternFormMap.put("11x", "AA|op");
            patternFormMap.put("10t", "AA|op");
            patternFormMap.put("20t", "ØØ|op AAAA");
            patternFormMap.put("20bc", "AA|op BBBB");
            patternFormMap.put("22x", "AA|op BBBB");
            patternFormMap.put("21t", "AA|op BBBB");
            patternFormMap.put("21s", "AA|op BBBB");
            patternFormMap.put("21h", "AA|op BBBB");
            patternFormMap.put("21c", "AA|op BBBB");
            patternFormMap.put("23x", "AA|op CC|BB");
            patternFormMap.put("22b", "AA|op CC|BB");
            patternFormMap.put("22t", "B|A|op CCCC");
            patternFormMap.put("22s", "B|A|op CCCC");
            patternFormMap.put("22c", "B|A|op CCCC");
            patternFormMap.put("22cs", "B|A|op CCCC");
            patternFormMap.put("30t", "ØØ|op AAAAloAAAAhi");
            patternFormMap.put("32x", "ØØ|op AAAA BBBB");
            patternFormMap.put("31i", "AA|op BBBBloBBBBhi");
            patternFormMap.put("31t", "AA|op BBBBloBBBBhi");
            patternFormMap.put("31c", "AA|op BBBBloBBBBhi");
            patternFormMap.put("35c", "A|G|op BBBB F|E|D|C");
            patternFormMap.put("35ms", "A|G|op BBBB F|E|D|C");
            patternFormMap.put("35mi", "A|G|op BBBB F|E|D|C");
            patternFormMap.put("3rc", "AA|op BBBB CCCC");
            patternFormMap.put("3rms", "AA|op BBBB CCCC");
            patternFormMap.put("3rmi", "AA|op BBBB CCCC");
            patternFormMap.put("51l", "AA|op BBBBloBBBB BBBB BBBBhi");
            patternFormMap.put("33x", "exop BB|AA CCCC");
            patternFormMap.put("32s", "exop BB|AA CCCC");
            patternFormMap.put("40sc", "exop BBBBloBBBBhiAAAA");
            patternFormMap.put("41c", "exop BBBBloBBBBhiAAAA  ");
            patternFormMap.put("52c", "exop CCCCloCCCChi    AAAA BBBB");
            patternFormMap.put("5rc", "exop BBBBloBBBBhi    AAAA CCCC");
        }
        return patternFormMap;
    }

    public static Map<Integer, DvmOpcode> getDvmOpCodeMap() {
        if (dvmOpCodeMap.size() == 0) {
            dvmOpCodeMap.put(0x00, new DvmOpcode(0x00, "10x", "nop"));
            dvmOpCodeMap.put(0x01, new DvmOpcode(0x01, "12x", "move "));
            dvmOpCodeMap.put(0x02, new DvmOpcode(0x02, "22x", "move/from16 "));
            dvmOpCodeMap.put(0x03, new DvmOpcode(0x03, "32x", "move/16 "));
            dvmOpCodeMap.put(0x04, new DvmOpcode(0x04, "12x", "move-wide "));
            dvmOpCodeMap.put(0x05, new DvmOpcode(0x05, "22x", "move-wide/from16 "));
            dvmOpCodeMap.put(0x06, new DvmOpcode(0x06, "32x", "move-wide/16 "));
            dvmOpCodeMap.put(0x07, new DvmOpcode(0x07, "12x", "move-object "));
            dvmOpCodeMap.put(0x08, new DvmOpcode(0x08, "22x", "move-object/from16 "));
            dvmOpCodeMap.put(0x09, new DvmOpcode(0x09, "32x", "move-object/16 "));
            dvmOpCodeMap.put(0x0a, new DvmOpcode(0x0a, "11x", "move-result "));
            dvmOpCodeMap.put(0x0b, new DvmOpcode(0x0b, "11x", "move-result-wide "));
            dvmOpCodeMap.put(0x0c, new DvmOpcode(0x0c, "11x", "move-result-object "));
            dvmOpCodeMap.put(0x0d, new DvmOpcode(0x0d, "11x", "move-exception "));
            dvmOpCodeMap.put(0x0e, new DvmOpcode(0x0e, "10x", "return-void"));
            dvmOpCodeMap.put(0x0f, new DvmOpcode(0x0f, "11x", "return "));
            dvmOpCodeMap.put(0x10, new DvmOpcode(0x10, "11x", "return-wide "));
            dvmOpCodeMap.put(0x11, new DvmOpcode(0x11, "11x", "return-object "));
            dvmOpCodeMap.put(0x12, new DvmOpcode(0x12, "11n", "const/4 "));
            dvmOpCodeMap.put(0x13, new DvmOpcode(0x13, "21s", "const/16 "));
            dvmOpCodeMap.put(0x14, new DvmOpcode(0x14, "31i", "const "));
            dvmOpCodeMap.put(0x15, new DvmOpcode(0x15, "21h", "const/high16 "));
            dvmOpCodeMap.put(0x16, new DvmOpcode(0x16, "21s", "const-wide/16 "));
            dvmOpCodeMap.put(0x17, new DvmOpcode(0x17, "31i", "const-wide/32 "));
            dvmOpCodeMap.put(0x18, new DvmOpcode(0x18, "51l", "const-wide "));
            dvmOpCodeMap.put(0x19, new DvmOpcode(0x19, "21h", "const-wide/high16 "));
            dvmOpCodeMap.put(0x1a, new DvmOpcode(0x1a, "21c", "const-string "));
            dvmOpCodeMap.put(0x1b, new DvmOpcode(0x1b, "31c", "const-string/jumbo "));
            dvmOpCodeMap.put(0x1c, new DvmOpcode(0x1c, "21c", "const-class "));
            dvmOpCodeMap.put(0x1d, new DvmOpcode(0x1d, "11x", "monitor-enter "));
            dvmOpCodeMap.put(0x1e, new DvmOpcode(0x1e, "11x", "monitor-exit "));
            dvmOpCodeMap.put(0x1f, new DvmOpcode(0x1f, "21c", "check-cast "));
            dvmOpCodeMap.put(0x20, new DvmOpcode(0x20, "22c", "instance-of "));
            dvmOpCodeMap.put(0x21, new DvmOpcode(0x21, "12x", "array-length "));
            dvmOpCodeMap.put(0x22, new DvmOpcode(0x22, "21c", "new-instance "));
            dvmOpCodeMap.put(0x23, new DvmOpcode(0x23, "22c", "new-array "));
            dvmOpCodeMap.put(0x24, new DvmOpcode(0x24, "35c", "filled-new-array "));
            dvmOpCodeMap.put(0x25, new DvmOpcode(0x25, "3rc", "filled-new-array/range "));
            dvmOpCodeMap.put(0x26, new DvmOpcode(0x26, "31t", "fill-array-data "));
            dvmOpCodeMap.put(0x27, new DvmOpcode(0x27, "11x", "throw "));
            dvmOpCodeMap.put(0x28, new DvmOpcode(0x28, "10t", "goto "));
            dvmOpCodeMap.put(0x29, new DvmOpcode(0x29, "20t", "goto/16 "));
            dvmOpCodeMap.put(0x2a, new DvmOpcode(0x2a, "30t", "goto/32 "));
            dvmOpCodeMap.put(0x2b, new DvmOpcode(0x2b, "31t", "packed-switch "));
            dvmOpCodeMap.put(0x2c, new DvmOpcode(0x2c, "31t", "sparse-switch "));
            dvmOpCodeMap.put(0x2d, new DvmOpcode(0x2d, "23x", " cmpl-float (lt"));
            dvmOpCodeMap.put(0x2e, new DvmOpcode(0x2e, "23x", " cmpg-float (gt"));
            dvmOpCodeMap.put(0x2f, new DvmOpcode(0x2f, "23x", " cmpl-double (lt"));
            dvmOpCodeMap.put(0x30, new DvmOpcode(0x30, "23x", " cmpg-double (gt"));
            dvmOpCodeMap.put(0x31, new DvmOpcode(0x31, "23x", "cmp-long"));
            dvmOpCodeMap.put(0x32, new DvmOpcode(0x32, "22t", "if-eq"));
            dvmOpCodeMap.put(0x33, new DvmOpcode(0x33, "22t", "if-ne"));
            dvmOpCodeMap.put(0x34, new DvmOpcode(0x34, "22t", "if-lt"));
            dvmOpCodeMap.put(0x35, new DvmOpcode(0x35, "22t", "if-ge"));
            dvmOpCodeMap.put(0x36, new DvmOpcode(0x36, "22t", "if-gt"));
            dvmOpCodeMap.put(0x37, new DvmOpcode(0x37, "22t", "if-le"));
            dvmOpCodeMap.put(0x38, new DvmOpcode(0x38, "21t", "if-eqz"));
            dvmOpCodeMap.put(0x39, new DvmOpcode(0x39, "21t", "if-nez"));
            dvmOpCodeMap.put(0x3a, new DvmOpcode(0x3a, "21t", "if-ltz"));
            dvmOpCodeMap.put(0x3b, new DvmOpcode(0x3b, "21t", "if-gez"));
            dvmOpCodeMap.put(0x3c, new DvmOpcode(0x3c, "21t", "if-gtz"));
            dvmOpCodeMap.put(0x3d, new DvmOpcode(0x3d, "21t", "if-lez"));
            dvmOpCodeMap.put(0x44, new DvmOpcode(0x44, "23x", "aget"));
            dvmOpCodeMap.put(0x45, new DvmOpcode(0x45, "23x", "aget-wide"));
            dvmOpCodeMap.put(0x46, new DvmOpcode(0x46, "23x", "aget-object"));
            dvmOpCodeMap.put(0x47, new DvmOpcode(0x47, "23x", "aget-boolean"));
            dvmOpCodeMap.put(0x48, new DvmOpcode(0x48, "23x", "aget-byte"));
            dvmOpCodeMap.put(0x49, new DvmOpcode(0x49, "23x", "aget-char"));
            dvmOpCodeMap.put(0x4a, new DvmOpcode(0x4a, "23x", "aget-short"));
            dvmOpCodeMap.put(0x4b, new DvmOpcode(0x4b, "23x", "aput"));
            dvmOpCodeMap.put(0x4c, new DvmOpcode(0x4c, "23x", "aput-wide"));
            dvmOpCodeMap.put(0x4d, new DvmOpcode(0x4d, "23x", "aput-object"));
            dvmOpCodeMap.put(0x4e, new DvmOpcode(0x4e, "23x", "aput-boolean"));
            dvmOpCodeMap.put(0x4f, new DvmOpcode(0x4f, "23x", "aput-byte"));
            dvmOpCodeMap.put(0x50, new DvmOpcode(0x50, "23x", "aput-char"));
            dvmOpCodeMap.put(0x51, new DvmOpcode(0x51, "23x", "aput-short"));
            dvmOpCodeMap.put(0x52, new DvmOpcode(0x52, "22c", "iget"));
            dvmOpCodeMap.put(0x53, new DvmOpcode(0x53, "22c", "iget-wide"));
            dvmOpCodeMap.put(0x54, new DvmOpcode(0x54, "22c", "iget-object"));
            dvmOpCodeMap.put(0x55, new DvmOpcode(0x55, "22c", "iget-boolean"));
            dvmOpCodeMap.put(0x56, new DvmOpcode(0x56, "22c", "iget-byte"));
            dvmOpCodeMap.put(0x57, new DvmOpcode(0x57, "22c", "iget-char"));
            dvmOpCodeMap.put(0x58, new DvmOpcode(0x58, "22c", "iget-short"));
            dvmOpCodeMap.put(0x59, new DvmOpcode(0x59, "22c", "iput"));
            dvmOpCodeMap.put(0x5a, new DvmOpcode(0x5a, "22c", "iput-wide"));
            dvmOpCodeMap.put(0x5b, new DvmOpcode(0x5b, "22c", "iput-object"));
            dvmOpCodeMap.put(0x5c, new DvmOpcode(0x5c, "22c", "iput-boolean"));
            dvmOpCodeMap.put(0x5d, new DvmOpcode(0x5d, "22c", "iput-byte"));
            dvmOpCodeMap.put(0x5e, new DvmOpcode(0x5e, "22c", "iput-char"));
            dvmOpCodeMap.put(0x5f, new DvmOpcode(0x5f, "22c", "iput-short"));
            dvmOpCodeMap.put(0x60, new DvmOpcode(0x60, "21c", "sget"));
            dvmOpCodeMap.put(0x61, new DvmOpcode(0x61, "21c", "sget-wide"));
            dvmOpCodeMap.put(0x62, new DvmOpcode(0x62, "21c", "sget-object"));
            dvmOpCodeMap.put(0x63, new DvmOpcode(0x63, "21c", "sget-boolean"));
            dvmOpCodeMap.put(0x64, new DvmOpcode(0x64, "21c", "sget-byte"));
            dvmOpCodeMap.put(0x65, new DvmOpcode(0x65, "21c", "sget-char"));
            dvmOpCodeMap.put(0x66, new DvmOpcode(0x66, "21c", "sget-short"));
            dvmOpCodeMap.put(0x67, new DvmOpcode(0x67, "21c", "sput"));
            dvmOpCodeMap.put(0x68, new DvmOpcode(0x68, "21c", "sput-wide"));
            dvmOpCodeMap.put(0x69, new DvmOpcode(0x69, "21c", "sput-object"));
            dvmOpCodeMap.put(0x6a, new DvmOpcode(0x6a, "21c", "sput-boolean"));
            dvmOpCodeMap.put(0x6b, new DvmOpcode(0x6b, "21c", "sput-byte"));
            dvmOpCodeMap.put(0x6c, new DvmOpcode(0x6c, "21c", "sput-char"));
            dvmOpCodeMap.put(0x6d, new DvmOpcode(0x6d, "21c", "sput-short"));
            dvmOpCodeMap.put(0x6e, new DvmOpcode(0x6e, "35c", "invoke-virtual"));
            dvmOpCodeMap.put(0x6f, new DvmOpcode(0x6f, "35c", "invoke-super"));
            dvmOpCodeMap.put(0x70, new DvmOpcode(0x70, "35c", "invoke-direct"));
            dvmOpCodeMap.put(0x71, new DvmOpcode(0x71, "35c", "invoke-static"));
            dvmOpCodeMap.put(0x72, new DvmOpcode(0x72, "35c", "invoke-interface"));
            dvmOpCodeMap.put(0x74, new DvmOpcode(0x74, "3rc", "invoke-virtual/range"));
            dvmOpCodeMap.put(0x75, new DvmOpcode(0x75, "3rc", "invoke-super/range"));
            dvmOpCodeMap.put(0x76, new DvmOpcode(0x76, "3rc", "invoke-direct/range"));
            dvmOpCodeMap.put(0x77, new DvmOpcode(0x77, "3rc", "invoke-static/range"));
            dvmOpCodeMap.put(0x78, new DvmOpcode(0x78, "3rc", "invoke-interface/range"));
            dvmOpCodeMap.put(0x7b, new DvmOpcode(0x7b, "12x", "neg-int"));
            dvmOpCodeMap.put(0x7c, new DvmOpcode(0x7c, "12x", "not-int"));
            dvmOpCodeMap.put(0x7d, new DvmOpcode(0x7d, "12x", "neg-long"));
            dvmOpCodeMap.put(0x7e, new DvmOpcode(0x7e, "12x", "not-long"));
            dvmOpCodeMap.put(0x7f, new DvmOpcode(0x7f, "12x", "neg-float"));
            dvmOpCodeMap.put(0x80, new DvmOpcode(0x80, "12x", "neg-double"));
            dvmOpCodeMap.put(0x81, new DvmOpcode(0x81, "12x", "int-to-long"));
            dvmOpCodeMap.put(0x82, new DvmOpcode(0x82, "12x", "int-to-float"));
            dvmOpCodeMap.put(0x83, new DvmOpcode(0x83, "12x", "int-to-double"));
            dvmOpCodeMap.put(0x84, new DvmOpcode(0x84, "12x", "long-to-int"));
            dvmOpCodeMap.put(0x85, new DvmOpcode(0x85, "12x", "long-to-float"));
            dvmOpCodeMap.put(0x86, new DvmOpcode(0x86, "12x", "long-to-double"));
            dvmOpCodeMap.put(0x87, new DvmOpcode(0x87, "12x", "float-to-int"));
            dvmOpCodeMap.put(0x88, new DvmOpcode(0x88, "12x", "float-to-long"));
            dvmOpCodeMap.put(0x89, new DvmOpcode(0x89, "12x", "float-to-double"));
            dvmOpCodeMap.put(0x8a, new DvmOpcode(0x8a, "12x", "double-to-int"));
            dvmOpCodeMap.put(0x8b, new DvmOpcode(0x8b, "12x", "double-to-long"));
            dvmOpCodeMap.put(0x8c, new DvmOpcode(0x8c, "12x", "double-to-float"));
            dvmOpCodeMap.put(0x8d, new DvmOpcode(0x8d, "12x", "int-to-byte"));
            dvmOpCodeMap.put(0x8e, new DvmOpcode(0x8e, "12x", "int-to-char"));
            dvmOpCodeMap.put(0x8f, new DvmOpcode(0x8f, "12x", "int-to-short"));
            dvmOpCodeMap.put(0x90, new DvmOpcode(0x90, "23x", "add-int"));
            dvmOpCodeMap.put(0x91, new DvmOpcode(0x91, "23x", "sub-int"));
            dvmOpCodeMap.put(0x92, new DvmOpcode(0x92, "23x", "mul-int"));
            dvmOpCodeMap.put(0x93, new DvmOpcode(0x93, "23x", "div-int"));
            dvmOpCodeMap.put(0x94, new DvmOpcode(0x94, "23x", "rem-int"));
            dvmOpCodeMap.put(0x95, new DvmOpcode(0x95, "23x", "and-int"));
            dvmOpCodeMap.put(0x96, new DvmOpcode(0x96, "23x", "or-int"));
            dvmOpCodeMap.put(0x97, new DvmOpcode(0x97, "23x", "xor-int"));
            dvmOpCodeMap.put(0x98, new DvmOpcode(0x98, "23x", "shl-int"));
            dvmOpCodeMap.put(0x99, new DvmOpcode(0x99, "23x", "shr-int"));
            dvmOpCodeMap.put(0x9a, new DvmOpcode(0x9a, "23x", "ushr-int"));
            dvmOpCodeMap.put(0x9b, new DvmOpcode(0x9b, "23x", "add-long"));
            dvmOpCodeMap.put(0x9c, new DvmOpcode(0x9c, "23x", "sub-long"));
            dvmOpCodeMap.put(0x9d, new DvmOpcode(0x9d, "23x", "mul-long"));
            dvmOpCodeMap.put(0x9e, new DvmOpcode(0x9e, "23x", "div-long"));
            dvmOpCodeMap.put(0x9f, new DvmOpcode(0x9f, "23x", "rem-long"));
            dvmOpCodeMap.put(0xa0, new DvmOpcode(0xa0, "23x", "and-long"));
            dvmOpCodeMap.put(0xa1, new DvmOpcode(0xa1, "23x", "or-long"));
            dvmOpCodeMap.put(0xa2, new DvmOpcode(0xa2, "23x", "xor-long"));
            dvmOpCodeMap.put(0xa3, new DvmOpcode(0xa3, "23x", "shl-long"));
            dvmOpCodeMap.put(0xa4, new DvmOpcode(0xa4, "23x", "shr-long"));
            dvmOpCodeMap.put(0xa5, new DvmOpcode(0xa5, "23x", "ushr-long"));
            dvmOpCodeMap.put(0xa6, new DvmOpcode(0xa6, "23x", "add-float"));
            dvmOpCodeMap.put(0xa7, new DvmOpcode(0xa7, "23x", "sub-float"));
            dvmOpCodeMap.put(0xa8, new DvmOpcode(0xa8, "23x", "mul-float"));
            dvmOpCodeMap.put(0xa9, new DvmOpcode(0xa9, "23x", "div-float"));
            dvmOpCodeMap.put(0xaa, new DvmOpcode(0xaa, "23x", "rem-float"));
            dvmOpCodeMap.put(0xab, new DvmOpcode(0xab, "23x", "add-double"));
            dvmOpCodeMap.put(0xac, new DvmOpcode(0xac, "23x", "sub-double"));
            dvmOpCodeMap.put(0xad, new DvmOpcode(0xad, "23x", "mul-double"));
            dvmOpCodeMap.put(0xae, new DvmOpcode(0xae, "23x", "div-double"));
            dvmOpCodeMap.put(0xaf, new DvmOpcode(0xaf, "23x", "rem-double"));
            dvmOpCodeMap.put(0xb0, new DvmOpcode(0xb0, "12x", "add-int/2addr"));
            dvmOpCodeMap.put(0xb1, new DvmOpcode(0xb1, "12x", "sub-int/2addr"));
            dvmOpCodeMap.put(0xb2, new DvmOpcode(0xb2, "12x", "mul-int/2addr"));
            dvmOpCodeMap.put(0xb3, new DvmOpcode(0xb3, "12x", "div-int/2addr"));
            dvmOpCodeMap.put(0xb4, new DvmOpcode(0xb4, "12x", "rem-int/2addr"));
            dvmOpCodeMap.put(0xb5, new DvmOpcode(0xb5, "12x", "and-int/2addr"));
            dvmOpCodeMap.put(0xb6, new DvmOpcode(0xb6, "12x", "or-int/2addr"));
            dvmOpCodeMap.put(0xb7, new DvmOpcode(0xb7, "12x", "xor-int/2addr"));
            dvmOpCodeMap.put(0xb8, new DvmOpcode(0xb8, "12x", "shl-int/2addr"));
            dvmOpCodeMap.put(0xb9, new DvmOpcode(0xb9, "12x", "shr-int/2addr"));
            dvmOpCodeMap.put(0xba, new DvmOpcode(0xba, "12x", "ushr-int/2addr"));
            dvmOpCodeMap.put(0xbb, new DvmOpcode(0xbb, "12x", "add-long/2addr"));
            dvmOpCodeMap.put(0xbc, new DvmOpcode(0xbc, "12x", "sub-long/2addr"));
            dvmOpCodeMap.put(0xbd, new DvmOpcode(0xbd, "12x", "mul-long/2addr"));
            dvmOpCodeMap.put(0xbe, new DvmOpcode(0xbe, "12x", "div-long/2addr"));
            dvmOpCodeMap.put(0xbf, new DvmOpcode(0xbf, "12x", "rem-long/2addr"));
            dvmOpCodeMap.put(0xc0, new DvmOpcode(0xc0, "12x", "and-long/2addr"));
            dvmOpCodeMap.put(0xc1, new DvmOpcode(0xc1, "12x", "or-long/2addr"));
            dvmOpCodeMap.put(0xc2, new DvmOpcode(0xc2, "12x", "xor-long/2addr"));
            dvmOpCodeMap.put(0xc3, new DvmOpcode(0xc3, "12x", "shl-long/2addr"));
            dvmOpCodeMap.put(0xc4, new DvmOpcode(0xc4, "12x", "shr-long/2addr"));
            dvmOpCodeMap.put(0xc5, new DvmOpcode(0xc5, "12x", "ushr-long/2addr"));
            dvmOpCodeMap.put(0xc6, new DvmOpcode(0xc6, "12x", "add-float/2addr"));
            dvmOpCodeMap.put(0xc7, new DvmOpcode(0xc7, "12x", "sub-float/2addr"));
            dvmOpCodeMap.put(0xc8, new DvmOpcode(0xc8, "12x", "mul-float/2addr"));
            dvmOpCodeMap.put(0xc9, new DvmOpcode(0xc9, "12x", "div-float/2addr"));
            dvmOpCodeMap.put(0xca, new DvmOpcode(0xca, "12x", "rem-float/2addr"));
            dvmOpCodeMap.put(0xcb, new DvmOpcode(0xcb, "12x", "add-double/2addr"));
            dvmOpCodeMap.put(0xcc, new DvmOpcode(0xcc, "12x", "sub-double/2addr"));
            dvmOpCodeMap.put(0xcd, new DvmOpcode(0xcd, "12x", "mul-double/2addr"));
            dvmOpCodeMap.put(0xce, new DvmOpcode(0xce, "12x", "div-double/2addr"));
            dvmOpCodeMap.put(0xcf, new DvmOpcode(0xcf, "12x", "rem-double/2addr"));
            dvmOpCodeMap.put(0xd0, new DvmOpcode(0xd0, "22s", "add-int/lit16"));
            dvmOpCodeMap.put(0xd1, new DvmOpcode(0xd1, "22s", " rsub-int"));
            dvmOpCodeMap.put(0xd2, new DvmOpcode(0xd2, "22s", "mul-int/lit16"));
            dvmOpCodeMap.put(0xd3, new DvmOpcode(0xd3, "22s", "div-int/lit16"));
            dvmOpCodeMap.put(0xd4, new DvmOpcode(0xd4, "22s", "rem-int/lit16"));
            dvmOpCodeMap.put(0xd5, new DvmOpcode(0xd5, "22s", "and-int/lit16"));
            dvmOpCodeMap.put(0xd6, new DvmOpcode(0xd6, "22s", "or-int/lit16"));
            dvmOpCodeMap.put(0xd7, new DvmOpcode(0xd7, "22s", "xor-int/lit16"));
            dvmOpCodeMap.put(0xd8, new DvmOpcode(0xd8, "22b", "add-int/lit8"));
            dvmOpCodeMap.put(0xd9, new DvmOpcode(0xd9, "22b", "rsub-int/lit8"));
            dvmOpCodeMap.put(0xda, new DvmOpcode(0xda, "22b", "mul-int/lit8"));
            dvmOpCodeMap.put(0xdb, new DvmOpcode(0xdb, "22b", "div-int/lit8"));
            dvmOpCodeMap.put(0xdc, new DvmOpcode(0xdc, "22b", "rem-int/lit8"));
            dvmOpCodeMap.put(0xdd, new DvmOpcode(0xdd, "22b", "and-int/lit8"));
            dvmOpCodeMap.put(0xde, new DvmOpcode(0xde, "22b", "or-int/lit8"));
            dvmOpCodeMap.put(0xdf, new DvmOpcode(0xdf, "22b", "xor-int/lit8"));
            dvmOpCodeMap.put(0xe0, new DvmOpcode(0xe0, "22b", "shl-int/lit8"));
            dvmOpCodeMap.put(0xe1, new DvmOpcode(0xe1, "22b", "shr-int/lit8"));
            dvmOpCodeMap.put(0xe2, new DvmOpcode(0xe2, "22b", "ushr-int/lit8"));
            dvmOpCodeMap.put(0xfa, new DvmOpcode(0xfa, "5cc", "invoke-polymorphic "));
            dvmOpCodeMap.put(0xfb, new DvmOpcode(0xfb, "rcc", "invoke-polymorphic/range "));
            dvmOpCodeMap.put(0xfc, new DvmOpcode(0xfc, "35c", "invoke-custom "));
            dvmOpCodeMap.put(0xfd, new DvmOpcode(0xfd, "3rc", "invoke-custom/range "));
        }
        return dvmOpCodeMap;
    }

    public static Map<Integer, String> getOpMap() {
        if (opCodeMap.size() == 0) {
            HashMap<String, Integer> map = new HashMap<>();
            map.put("V1_1", 196653);
            map.put("V1_2", 46);
            map.put("V1_3", 47);
            map.put("V1_4", 48);
            map.put("V1_5", 49);
            map.put("V1_6", 50);
            map.put("V1_7", 51);
            map.put("ACC_PUBLIC", 1);
            map.put("ACC_PRIVATE", 2);
            map.put("ACC_PROTECTED", 4);
            map.put("ACC_STATIC", 8);
            map.put("ACC_FINAL", 16);
            map.put("ACC_SUPER", 32);
            map.put("ACC_SYNCHRONIZED", 32);
            map.put("ACC_VOLATILE", 64);
            map.put("ACC_BRIDGE", 64);
            map.put("ACC_VARARGS", 128);
            map.put("ACC_TRANSIENT", 128);
            map.put("ACC_NATIVE", 256);
            map.put("ACC_INTERFACE", 512);
            map.put("ACC_ABSTRACT", 1024);
            map.put("ACC_STRICT", 2048);
            map.put("ACC_SYNTHETIC", 4096);
            map.put("ACC_ANNOTATION", 8192);
            map.put("ACC_ENUM", 16384);
            map.put("ACC_DEPRECATED", 131072);
            map.put("T_BOOLEAN", 4);
            map.put("T_CHAR", 5);
            map.put("T_FLOAT", 6);
            map.put("T_DOUBLE", 7);
            map.put("T_BYTE", 8);
            map.put("T_SHORT", 9);
            map.put("T_INT", 10);
            map.put("T_LONG", 11);
            map.put("F_NEW", -1);
            map.put("F_FULL", 0);
            map.put("F_APPEND", 1);
            map.put("F_CHOP", 2);
            map.put("F_SAME", 3);
            map.put("F_SAME1", 4);
            map.put("TOP", 0);
            map.put("INTEGER", 1);
            map.put("FLOAT", 2);
            map.put("DOUBLE", 3);
            map.put("LONG", 4);
            map.put("NULL", 5);
            map.put("UNINITIALIZED_THIS", 6);
            map.put("NOP", 0);
            map.put("ACONST_NULL", 1);
            map.put("ICONST_M1", 2);
            map.put("ICONST_0", 3);
            map.put("ICONST_1", 4);
            map.put("ICONST_2", 5);
            map.put("ICONST_3", 6);
            map.put("ICONST_4", 7);
            map.put("ICONST_5", 8);
            map.put("LCONST_0", 9);
            map.put("LCONST_1", 10);
            map.put("FCONST_0", 11);
            map.put("FCONST_1", 12);
            map.put("FCONST_2", 13);
            map.put("DCONST_0", 14);
            map.put("DCONST_1", 15);
            map.put("BIPUSH", 16);
            map.put("SIPUSH", 17);
            map.put("LDC", 18);
            map.put("ILOAD", 21);
            map.put("LLOAD", 22);
            map.put("FLOAD", 23);
            map.put("DLOAD", 24);
            map.put("ALOAD", 25);
            map.put("IALOAD", 46);
            map.put("LALOAD", 47);
            map.put("FALOAD", 48);
            map.put("DALOAD", 49);
            map.put("AALOAD", 50);
            map.put("BALOAD", 51);
            map.put("CALOAD", 52);
            map.put("SALOAD", 53);
            map.put("ISTORE", 54);
            map.put("LSTORE", 55);
            map.put("FSTORE", 56);
            map.put("DSTORE", 57);
            map.put("ASTORE", 58);
            map.put("IASTORE", 79);
            map.put("LASTORE", 80);
            map.put("FASTORE", 81);
            map.put("DASTORE", 82);
            map.put("AASTORE", 83);
            map.put("BASTORE", 84);
            map.put("CASTORE", 85);
            map.put("SASTORE", 86);
            map.put("POP", 87);
            map.put("POP2", 88);
            map.put("DUP", 89);
            map.put("DUP_X1", 90);
            map.put("DUP_X2", 91);
            map.put("DUP2", 92);
            map.put("DUP2_X1", 93);
            map.put("DUP2_X2", 94);
            map.put("SWAP", 95);
            map.put("IADD", 96);
            map.put("LADD", 97);
            map.put("FADD", 98);
            map.put("DADD", 99);
            map.put("ISUB", 100);
            map.put("LSUB", 101);
            map.put("FSUB", 102);
            map.put("DSUB", 103);
            map.put("IMUL", 104);
            map.put("LMUL", 105);
            map.put("FMUL", 106);
            map.put("DMUL", 107);
            map.put("IDIV", 108);
            map.put("LDIV", 109);
            map.put("FDIV", 110);
            map.put("DDIV", 111);
            map.put("IREM", 112);
            map.put("LREM", 113);
            map.put("FREM", 114);
            map.put("DREM", 115);
            map.put("INEG", 116);
            map.put("LNEG", 117);
            map.put("FNEG", 118);
            map.put("DNEG", 119);
            map.put("ISHL", 120);
            map.put("LSHL", 121);
            map.put("ISHR", 122);
            map.put("LSHR", 123);
            map.put("IUSHR", 124);
            map.put("LUSHR", 125);
            map.put("IAND", 126);
            map.put("LAND", 127);
            map.put("IOR", 128);
            map.put("LOR", 129);
            map.put("IXOR", 130);
            map.put("LXOR", 131);
            map.put("IINC", 132);
            map.put("I2L", 133);
            map.put("I2F", 134);
            map.put("I2D", 135);
            map.put("L2I", 136);
            map.put("L2F", 137);
            map.put("L2D", 138);
            map.put("F2I", 139);
            map.put("F2L", 140);
            map.put("F2D", 141);
            map.put("D2I", 142);
            map.put("D2L", 143);
            map.put("D2F", 144);
            map.put("I2B", 145);
            map.put("I2C", 146);
            map.put("I2S", 147);
            map.put("LCMP", 148);
            map.put("FCMPL", 149);
            map.put("FCMPG", 150);
            map.put("DCMPL", 151);
            map.put("DCMPG", 152);
            map.put("IFEQ", 153);
            map.put("IFNE", 154);
            map.put("IFLT", 155);
            map.put("IFGE", 156);
            map.put("IFGT", 157);
            map.put("IFLE", 158);
            map.put("IF_ICMPEQ", 159);
            map.put("IF_ICMPNE", 160);
            map.put("IF_ICMPLT", 161);
            map.put("IF_ICMPGE", 162);
            map.put("IF_ICMPGT", 163);
            map.put("IF_ICMPLE", 164);
            map.put("IF_ACMPEQ", 165);
            map.put("IF_ACMPNE", 166);
            map.put("GOTO", 167);
            map.put("JSR", 168);
            map.put("RET", 169);
            map.put("TABLESWITCH", 170);
            map.put("LOOKUPSWITCH", 171);
            map.put("IRETURN", 172);
            map.put("LRETURN", 173);
            map.put("FRETURN", 174);
            map.put("DRETURN", 175);
            map.put("ARETURN", 176);
            map.put("RETURN", 177);
            map.put("GETSTATIC", 178);
            map.put("PUTSTATIC", 179);
            map.put("GETFIELD", 180);
            map.put("PUTFIELD", 181);
            map.put("INVOKEVIRTUAL", 182);
            map.put("INVOKESPECIAL", 183);
            map.put("INVOKESTATIC", 184);
            map.put("INVOKEINTERFACE", 185);
            map.put("INVOKEDYNAMIC", 186);
            map.put("NEW", 187);
            map.put("NEWARRAY", 188);
            map.put("ANEWARRAY", 189);
            map.put("ARRAYLENGTH", 190);
            map.put("ATHROW", 191);
            map.put("CHECKCAST", 192);
            map.put("INSTANCEOF", 193);
            map.put("MONITORENTER", 194);
            map.put("MONITOREXIT", 195);
            map.put("MULTIANEWARRAY", 197);
            map.put("IFNULL", 198);
            map.put("IFNONNULL", 199);
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                opCodeMap.put(entry.getValue(), entry.getKey());
            }
        }
        return opCodeMap;
    }

    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序。 和bytesToInt（）配套使用
     *
     * @param value 要转换的int值
     * @return byte数组
     */
    public static byte[] intToBytes(int value) {
        byte[] src = new byte[4];
        src[3] = (byte) ((value >> 24) & 0xFF);
        src[2] = (byte) ((value >> 16) & 0xFF);
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序。  和bytesToInt2（）配套使用
     */
    public static byte[] intToBytes2(int value) {
        byte[] src = new byte[4];
        src[0] = (byte) ((value >> 24) & 0xFF);
        src[1] = (byte) ((value >> 16) & 0xFF);
        src[2] = (byte) ((value >> 8) & 0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序。
     *
     * @param ary    byte数组
     * @param offset 从数组的第offset位开始
     * @return int数值
     */
    public static int bytesToInt(byte[] ary, int offset) {
        int value;
        value = (int) ((ary[offset] & 0xFF)
                | ((ary[offset + 1] << 8) & 0xFF00)
                | ((ary[offset + 2] << 16) & 0xFF0000)
                | ((ary[offset + 3] << 24) & 0xFF000000));
        return value;
    }

    public static String bytesToString(byte[] array, int start) {
        if (array == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        int u;
        for (int i = start; i < array.length; i++) {
            u = array[i] & 0xFF;
            if (u < 0x10) {
                sb.append(String.format("0%x", u)).append(" ");
            } else {
                sb.append(String.format("%x", u)).append(" ");
            }
        }
        return sb.toString();
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序。
     *
     * @param ary    byte数组
     * @param offset 从数组的第offset位开始
     * @return int数值
     */
    public static int u2ToInt(byte[] ary, int offset) {
        return (ary[offset] & 0xFF) | ((ary[offset + 1] & 0xFF) << 8);
    }

    public static int u2ToIntLittle(byte[] ary, int offset) {
        return (ary[offset + 1] & 0xFF) | ((ary[offset] & 0xFF) << 8);
    }

    public static String intsToStringBy4(int[] array, int start) {
        if (array == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        int times;
        for (int i = start; i < array.length; i++) {
            String digit = String.format("%x", array[i]);
            times = 4 - digit.length();
            for (int j = 0; j < times; j++) {
                digit = "0" + digit;
            }
            sb.append(digit).append(" ");
        }
        return sb.toString();
    }

    public static String bytesToInsnForm(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i += 2) {
            int a = data[i + 1] & 0xff;
            int b = data[i] & 0xff;
            String ap = "%x";
            String bp = "%x";
            if (a < 0x10) {
                ap = "0" + ap;
            }
            if (b < 0x10) {
                bp = "0" + bp;
            }
            sb.append(String.format(ap + bp + " ", a, b));
        }
        return sb.toString();
    }

    public static Integer hexToInt(String digit) {
        return Integer.parseInt(digit, 16);
    }
}
