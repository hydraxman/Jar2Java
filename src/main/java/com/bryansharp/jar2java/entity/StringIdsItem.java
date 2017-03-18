package com.bryansharp.jar2java.entity;

import com.bryansharp.jar2java.LogUtils;
import com.bryansharp.jar2java.Mutf8;

import java.io.UTFDataFormatException;

/**
 * Created by bsp on 17/3/18.
 */
public class StringIdsItem extends IntRefsItem {
    public StringIdsItem(String name) {
        super(name);
    }

    protected void parseBaseRealData(byte[] dexData) {
        String[] realData = new String[refs.length];
        for (int i = 0; i < realData.length; i++) {
            int start = refs[i];
            int end, len = 0;
            if (i != realData.length - 1) {
                end = refs[i + 1];
            } else {
                end = refs[i];
                while (dexData[end] != 0) {
                    end++;
                }
                end++;
            }
            len = end - start;
            byte[] content = new byte[len];
            System.arraycopy(dexData, start, content, 0, len);
            try {
                realData[i] = Mutf8.decode(content);
            } catch (UTFDataFormatException e) {
                e.printStackTrace();
            }
        }
        this.realData = realData;
    }

}
