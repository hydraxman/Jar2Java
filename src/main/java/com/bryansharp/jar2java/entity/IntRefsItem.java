package com.bryansharp.jar2java.entity;

import com.bryansharp.jar2java.LogUtils;
import com.bryansharp.jar2java.Mutf8;
import com.bryansharp.jar2java.Utils;
import com.bryansharp.jar2java.entity.base.DexDataItem;

import java.io.UTFDataFormatException;

/**
 * Created by bsp on 17/3/18.
 */
public class IntRefsItem extends DexDataItem<Integer, String> {
    public IntRefsItem(String name) {
        super(name);
    }

    @Override
    protected Integer[] createRefs() {
        return new Integer[count];
    }

    @Override
    protected int getRefSize() {
        return Integer.SIZE / Byte.SIZE;
    }


    protected Integer parseRef(int i) {
        return Utils.bytesToInt(data, i);
    }



}
