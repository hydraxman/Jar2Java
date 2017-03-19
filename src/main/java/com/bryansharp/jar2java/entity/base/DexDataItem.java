package com.bryansharp.jar2java.entity.base;

import com.bryansharp.jar2java.LogUtils;

import java.util.Map;

/**
 * Created by bushaopeng on 17/3/17.
 */
public abstract class DexDataItem<T, U> extends DexItem {

    protected T[] refs;
    public U[] realData;
    public int count;


    public DexDataItem(String name) {
        this.name = name;
    }

    public boolean readyToFill() {
        return byteSize > 0 && start > 0 && DexData.sizeIsCountNamesArr.contains(name);
    }

    protected abstract T[] createRefs();

    protected abstract int getRefSize();

    public void printData() {
        printTitle();
        if (data != null) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < refs.length; i++) {
                stringBuilder.append("\n").append(i).append(",").append(refs[i]).append(",");
                if (realData != null) {
                    stringBuilder.append(realData[i]);
                }
            }
            LogUtils.log(stringBuilder.toString());
        }
    }

    protected void printTitle() {
        LogUtils.log("\n" + name + " : byteSize is " + byteSize + " : count is " + count + " : start is " + start + " : data is :");
    }

    public void fillRefs() {
        int refSize = getRefSize();
        for (int i = 0; i < byteSize; i += refSize) {
            refs[i / refSize] = parseRef(i);
        }
    }

    public void fillData(byte[] dexData) {
        byte[] data = new byte[byteSize];
        System.arraycopy(dexData, start, data, 0, byteSize);
        this.data = data;
    }

    protected void parseBaseRealData(byte[] dexData) {
    }

    protected void parse1stRealData(Map<String, DexDataItem> dataItems, byte[] dexData) {
    }

    protected abstract T parseRef(int i);

    public void parse2ndRealData(Map<String, DexDataItem> dataItems, byte[] dexData) {

    }

    public void parse3rdRealData(Map<String, DexDataItem> dataItems, byte[] dexData) {

    }

    public void parse4thRealData(Map<String, DexDataItem> dataItems, byte[] dexData) {
    }
}
