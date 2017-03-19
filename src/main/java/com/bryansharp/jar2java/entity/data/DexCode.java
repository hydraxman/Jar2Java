package com.bryansharp.jar2java.entity.data;

import com.bryansharp.jar2java.Utils;
import com.bryansharp.jar2java.entity.base.DexDataItem;
import com.bryansharp.jar2java.entity.base.DvmOpcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by bsp on 17/3/19.
 */
public class DexCode {
    public int registersSize;
    public int insSize;
    public int outsSize;
    public int triesSize;
    public int debugInfoOff;
    public int insnsSize;
    public int[] insns;
    public byte[] insnsRaw;
    public List<DexCodeInsn> insnList = new ArrayList<>();

    @Override
    public String toString() {
        return "\n\t\t\tDexCode{" +
                "registersSize=" + registersSize +
                ", insSize=" + insSize +
                ", outsSize=" + outsSize +
                ", triesSize=" + triesSize +
                ", debugInfoOff=" + debugInfoOff +
                ", insnsSize=" + insnsSize +
                ", insns=" + Utils.intsToStringBy4(insns, 0) +
                ", insnList=" + insnList +
                '}';
    }

    public void parseInsns(Map<String, DexDataItem> dataItems) {
        int start = 0;
        DexCodeInsn dexCodeInsn;
        while (start < insnsRaw.length) {
            byte op = insnsRaw[start];
            DvmOpcode dvmOpcode = Utils.getDvmOpCodeMap().get(op & 0xff);
            dexCodeInsn = new DexCodeInsn();
            insnList.add(dexCodeInsn);
            dexCodeInsn.op = dvmOpcode;
            int len = dvmOpcode.opSize * 2;
            dexCodeInsn.data = new byte[len];
            System.arraycopy(insnsRaw, start, dexCodeInsn.data, 0, len);
            dexCodeInsn.parseInsn(dataItems);
            start += len;
        }
    }
}
