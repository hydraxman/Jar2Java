package com.bryansharp.jar2java;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by bushaopeng on 17/1/24.
 */
public class Utils {
    public static HashMap<Integer, String> accCodeMap = new HashMap<>();
    static HashMap<Integer, String> opCodeMap = new HashMap<>();
    static HashSet<String> keywords = new HashSet<>();

    static {
        keywords.add("do");
        keywords.add("if");
        keywords.add("for");
        keywords.add("int");
        keywords.add("new");
        keywords.add("try");
        keywords.add("assert");
        keywords.add("byte");
        keywords.add("case");
        keywords.add("char");
        keywords.add("else");
        keywords.add("goto");
        keywords.add("long");
        keywords.add("this");
        keywords.add("void");
        keywords.add("break");
        keywords.add("catch");
        keywords.add("class");
        keywords.add("const");
        keywords.add("final");
        keywords.add("float");
        keywords.add("short");
        keywords.add("super");
        keywords.add("throw");
        keywords.add("while");
        keywords.add("double");
        keywords.add("import");
        keywords.add("native");
        keywords.add("public");
        keywords.add("return");
        keywords.add("static");
        keywords.add("switch");
        keywords.add("throws");
        keywords.add("boolean");
        keywords.add("default");
        keywords.add("extends");
        keywords.add("finally");
        keywords.add("package");
        keywords.add("private");
        keywords.add("abstract");
        keywords.add("continue");
        keywords.add("strictfp");
        keywords.add("volatile");
        keywords.add("interface");
        keywords.add("protected");
        keywords.add("transient");
        keywords.add("implements");
        keywords.add("instanceof");
        keywords.add("synchronized");

    }

    public static String path2Classname(String entryName) {
        return entryName.replace(File.separator, ".").replace(".class", "");
    }

    public static String classname2Path(String oldClassname) {
        return oldClassname.replace(".", "/");
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

    public static Map<Integer, String> getAccCodeMap() {
        if (accCodeMap.size() == 0) {
            HashMap<String, Integer> map = new HashMap<>();
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
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                accCodeMap.put(entry.getValue(), entry.getKey());
            }
        }
        return accCodeMap;
    }

    public static String accCode2String(int access) {
        StringBuilder builder = new StringBuilder();
        Map<Integer, String> map = getAccCodeMap();
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if ((entry.getKey() & access) > 0) {
                //此处如果使用|作为分隔符会导致编译报错 因此改用斜杠
                builder.append('\\').append(entry.getValue()).append("/ ");
            }
        }
        return builder.toString();
    }

    public static String accCode2StringPrettry(int access) {
        StringBuilder builder = new StringBuilder();
        Map<Integer, String> map = getAccCodeMap();
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if ((entry.getKey() & access) > 0) {
                //此处如果使用|作为分隔符会导致编译报错 因此改用斜杠
                builder.append(entry.getValue()).append(" ");
            }
        }
        return builder.toString();
    }

    public static String getOpName(int opCode) {
        return getOpMap().get(opCode);
    }

    public static void logEach(Object... msg) {
        for (Object m : msg) {
            try {
                if (m != null) {
                    if (m.getClass().isArray()) {
                        logInline("[");
                        int length = Array.getLength(m);
                        if (length > 0) {
                            for (int i = 0; i < length; i++) {
                                Object get = Array.get(m, i);
                                if (get != null) {
                                    logInline(get + "\t");
                                } else {
                                    logInline("null\t");
                                }
                            }
                        }
                        logInline("]\t");
                    } else {
                        logInline(m + "\t");
                    }
                } else {
                    logInline("null\t");
                }
            } catch (Exception e) {
            }
        }
        logInline("\n");
    }

    public static void log(Object msg) {
        System.out.println(msg);
    }

    public static void logInline(Object msg) {
        System.out.print(msg);
    }

    public static boolean isProguardedName(String simpleName) {
        if (simpleName.matches("[a-z]{1,2}")) {
            return true;
        }
        if (Utils.isKeyWord(simpleName)) {
            return true;
        }
        return false;
    }

    public static boolean isKeyWord(String simpleName) {
        return keywords.contains(simpleName);
    }

    public static boolean isPublicStatic(Integer arg) {
        int count = 0;
        Map<Integer, String> map = getAccCodeMap();
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if ((entry.getKey() & arg) > 0) {
                //此处如果使用|作为分隔符会导致编译报错 因此改用斜杠
                String value = entry.getValue();
                if (value.equals("ACC_PUBLIC")) {
                    count++;
                }
                if (value.equals("ACC_STATIC")) {
                    count++;
                }
            }
        }
        return count == 2;
    }

    public static String className2Path(String s) {
        return s.replace('.', '/');
    }

    public static File unzipEntryToTemp(ZipEntry element, ZipFile zipFile) throws IOException {
        InputStream stream = zipFile.getInputStream(element);
        byte[] array = IOUtils.toByteArray(stream);
        String hex = DigestUtils.md5Hex(element.getName());
        final File tempDir = new File(Main.getProjectPath() + "/build");
        File targetFile = new File(tempDir, hex + ".jar");
        if (targetFile.exists()) {
            targetFile.delete();
        }
        new FileOutputStream(targetFile).write(array);
        return targetFile;
    }

    public static void logProxy(Method method, Object[] args, Object returnedVal, int indent) {
        StringBuilder builder = new StringBuilder();
        String methodName = method.getName();
        if (args != null && args.length > 0) {
            int count = 0;
            for (Object arg : args) {
                if (arg != null) {

                    if (methodName.endsWith("Insn") && count == 0) {
                        if (arg instanceof Integer) {
                            builder.append(Utils.getOpName((Integer) arg));
                        } else {
                            builder.append("->" + arg);
                        }
                    } else if ("visitMethod".equals(methodName) && count == 0 && arg instanceof Integer) {
                        builder.append(Utils.accCode2String((Integer) arg));
                    } else {
                        builder.append(arg.toString());
                    }
                } else {
                    builder.append("null");
                }
                builder.append("\t");
                count++;
            }
            builder.setLength(builder.length() - 1);
        }
        if (returnedVal != null) {
            builder.append(", 返回值：").append(returnedVal);
        } else {
            builder.append("，无返回值");
        }
        Utils.log((indent == 0 ? "" : "\t") + "调用：" + methodName + ", 参数: " + builder.toString());
    }

    public static boolean isPublic(int access) {
        Map<Integer, String> map = getAccCodeMap();
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if ((entry.getKey() & access) > 0) {
                //此处如果使用|作为分隔符会导致编译报错 因此改用斜杠
                String value = entry.getValue();
                if (value.equals("ACC_PUBLIC")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isPrivate(int access) {
        Map<Integer, String> map = getAccCodeMap();
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if ((entry.getKey() & access) > 0) {
                //此处如果使用|作为分隔符会导致编译报错 因此改用斜杠
                String value = entry.getValue();
                if (value.equals("ACC_PRIVATE")) {
                    return true;
                }
            }
        }
        return false;
    }
}
