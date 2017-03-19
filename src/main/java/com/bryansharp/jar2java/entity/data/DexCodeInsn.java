package com.bryansharp.jar2java.entity.data;

import com.bryansharp.jar2java.Utils;
import com.bryansharp.jar2java.entity.base.DvmOpcode;

import java.util.Arrays;

/**
 * Created by bsp on 17/3/19.
 */
public class DexCodeInsn {
    public DvmOpcode op;
    public byte[] data;
    public String desc;
    public int A;
    public int B;

    @Override
    public String toString() {
        return "\n\t\t\tDexCodeInsn{" +
                "op=" + op +
                ", data=" + Utils.bytesToInsnForm(data) +
                ", desc='" + desc + '\'' +
                ", A=" + A +
                ", B=" + B +
                '}';
    }

    public void parseInsn() {
        byte ba = data[1];
        B = (ba & 0xF0) >> 4;
        A = ba & 0xF;
    }
}
