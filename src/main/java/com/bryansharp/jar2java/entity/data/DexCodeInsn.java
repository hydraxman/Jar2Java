package com.bryansharp.jar2java.entity.data;

import com.bryansharp.jar2java.Utils;
import com.bryansharp.jar2java.entity.base.DexData;
import com.bryansharp.jar2java.entity.base.DexDataItem;
import com.bryansharp.jar2java.entity.base.DvmOpcode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bsp on 17/3/19.
 */
public class DexCodeInsn {
    public DvmOpcode op;
    public byte[] data;
    public String desc;
    public Map<String, Integer> descMap = new HashMap<>();
    public Object opTarget;

    @Override
    public String toString() {
        return "\n\t\t\tDexCodeInsn{" +
                "op=" + op +
                ", data=" + Utils.bytesToInsnForm(data) +
                ", desc='" + desc + '\'' +
                ", descMap='" + descMap + '\'' +
                ", opTarget='" + opTarget + '\'' +
                '}';
    }

    public void parseInsn(Map<String, DexDataItem> dataItems) {
        String hexLine = Utils.bytesToInsnForm(data).replace(" ", "");
        String formForUse = op.formForUse;
        char p;
        char np;
        int repTimes = 0;
        int off = 0;
        for (int i = 0; i < formForUse.length(); i++) {
            p = formForUse.charAt(i);
            if (i < formForUse.length() - 1) {
                np = formForUse.charAt(i + 1);
                if (p != np) {
                    if (("" + p + np).equals("op")) {
                        String digit = hexLine.substring(off, off + 2);
                        off += 2;
                        descMap.put("op", Utils.hexToInt(digit));
                        i++;
                    } else {
                        String digit = hexLine.substring(off, off + repTimes + 1);
                        off += repTimes + 1;
                        descMap.put(p + "", Utils.hexToInt(digit));
                        repTimes = 0;
                    }
                } else {
                    repTimes++;
                }
            } else {
                String digit = hexLine.substring(off, off + repTimes + 1);
                descMap.put(p + "", Utils.hexToInt(digit));
            }
        }
        if (op.desc.contains("invok")) {
            Integer b = descMap.get("B");
            opTarget = dataItems.get(DexData.METHOD_IDS).realData[b];
        } else if ("const-string".equals(op.desc)) {
            Integer b = descMap.get("B");
            opTarget = dataItems.get(DexData.STRING_IDS).realData[b];
        }
    }
}
