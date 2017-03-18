package com.bryansharp.jar2java.entity;

import com.bryansharp.jar2java.LogUtils;
import com.bryansharp.jar2java.Utils;

/**
 * Created by bushaopeng on 17/3/17.
 */
public class DexDataItem extends DexItem {
    private static final int DATA_TYPE_ID_INDEX = 17;
    private int dataType;
    private int[] indexes;
    private String[] realData;

    public DexDataItem(String name) {
        super();
        this.name = name;

    }

    public boolean readyToFill() {
        return length > 0 && start > 0;
    }

    public void resetLengthByType() {
        if ("stringIds".equals(name)) {
            indexes = new int[length];
            length = length * 4;
            dataType = DATA_TYPE_ID_INDEX;
        }
    }

    public void printData() {
        if (data == null) {
            LogUtils.log(name + " : size is " + length + " : data is : null");
            return;
        }
        if (dataType == DATA_TYPE_ID_INDEX) {
            LogUtils.log(name + " : size is " + length + " : data is :");
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < indexes.length - 1; i++) {
                stringBuilder.append(indexes[i]).append(",").append(realData[i]).append("|");
            }
            LogUtils.log(stringBuilder.toString());
        } else {
            String dataStr = new String(data);
            LogUtils.log(name + " : size is " + length + " : data is :");
            LogUtils.log(dataStr);
        }

    }

    public void fillData(byte[] dexData) {
        byte[] data = new byte[length];
        System.arraycopy(dexData, start, data, 0, length);
        this.data = data;
        if (dataType == DATA_TYPE_ID_INDEX) {
            for (int i = 0; i < length; i += 4) {
                indexes[i / 4] = Utils.bytesToInt(data, i);
            }
            String[] realData = new String[indexes.length];
            for (int i = 0; i < realData.length - 1; i++) {
                int len = indexes[i + 1] - indexes[i];
                byte[] content = new byte[len];
                System.arraycopy(dexData, indexes[i], content, 0, len);
                realData[i] = new String(content);
            }
            this.realData = realData;
        } else {
        }
    }
}
