package com.bryansharp.jar2java;

/**
 * Created by bushaopeng on 17/2/8.
 */
public class LogUtils {
    public static void log(Object msg) {
        if (msg == null) {
            return;
        }
        System.out.println(msg.toString());
    }

    public static void logDiv() {
        System.out.println("==============================");
    }

    public static void logEach(Object... msgs) {
//        System.out.println("===============logEach===============");
        for (Object msg : msgs) {
            System.out.print(msg.toString());
            System.out.print("\t");
        }
        System.out.print("\n");
    }
    public static void logOpcode(int opCode) {
        LogUtils.log("指令："+ getOpName(opCode));
    }

    public static String getOpName(int opCode) {
        return Utils.getOpMap().get(opCode);
    }
}
