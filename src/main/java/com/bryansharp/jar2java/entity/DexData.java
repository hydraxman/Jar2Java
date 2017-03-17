package com.bryansharp.jar2java.entity;


import com.bryansharp.jar2java.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by bushaopeng on 17/3/14.
 */
public class DexData {
    private Map<String, DexHeadItem> headers = new LinkedHashMap<>();
    private Map<String, DexDataItem> dataItems = new HashMap<>();
    private byte[] dexData;

    public DexData() {
        ArrayList<DexHeadItem> headerList = new ArrayList<>();
        int start = 0;
        headerList.add(new DexHeadItem("magic", start, 8));
        start += 8;
        headerList.add(new DexHeadItem("checksum", start, 4));
        start += 0x14;
        headerList.add(new DexHeadItem("signature", start, 0x14));
        start += 4;
        headerList.add(new DexHeadItem("fileSize", start, 4));
        start += 4;
        headerList.add(new DexHeadItem("headerSize", start, 4));
        start += 4;
        headerList.add(new DexHeadItem("endianTag", start, 4));
        start += 4;
        headerList.add(new DexHeadItem("linkSize", start, 4, DexItem.TYPE_INT_SIZE));
        start += 4;
        headerList.add(new DexHeadItem("linkOff", start, 4, DexItem.TYPE_INT_OFFSET));
        start += 4;
        headerList.add(new DexHeadItem("mapOff", start, 4, DexItem.TYPE_INT_OFFSET));
        start += 4;
        headerList.add(new DexHeadItem("stringIdsSize", start, 4, DexItem.TYPE_INT_SIZE));
        start += 4;
        headerList.add(new DexHeadItem("stringIdsOff", start, 4, DexItem.TYPE_INT_OFFSET));
        start += 4;
        headerList.add(new DexHeadItem("typeIdsSize", start, 4, DexItem.TYPE_INT_SIZE));
        start += 4;
        headerList.add(new DexHeadItem("typeIdsOff", start, 4, DexItem.TYPE_INT_OFFSET));
        start += 4;
        headerList.add(new DexHeadItem("protoIdsSize", start, 4, DexItem.TYPE_INT_SIZE));
        start += 4;
        headerList.add(new DexHeadItem("protoIdsOff", start, 4, DexItem.TYPE_INT_OFFSET));
        start += 4;
        headerList.add(new DexHeadItem("fieldIdsSize", start, 4, DexItem.TYPE_INT_SIZE));
        start += 4;
        headerList.add(new DexHeadItem("fieldIdsOff", start, 4, DexItem.TYPE_INT_OFFSET));
        start += 4;
        headerList.add(new DexHeadItem("methodIdsSize", start, 4, DexItem.TYPE_INT_SIZE));
        start += 4;
        headerList.add(new DexHeadItem("methodIdsOff", start, 4, DexItem.TYPE_INT_OFFSET));
        start += 4;
        headerList.add(new DexHeadItem("classDefsSize", start, 4, DexItem.TYPE_INT_SIZE));
        start += 4;
        headerList.add(new DexHeadItem("classDefsOff", start, 4, DexItem.TYPE_INT_OFFSET));
        start += 4;
        headerList.add(new DexHeadItem("dataSize", start, 4, DexItem.TYPE_INT_SIZE));
        start += 4;
        headerList.add(new DexHeadItem("dataOff", start, 4, DexItem.TYPE_INT_OFFSET));
        int len = start + 4;
        LogUtils.log("header总长度：" + len);
        for (DexHeadItem header : headerList) {
            headers.put(header.name, header);
        }
    }

    public void fillHeaders(byte[] dexData) {
        this.dexData = dexData;
        byte[] data = null;
        for (Map.Entry<String, DexHeadItem> entry : headers.entrySet()) {
            DexHeadItem headItem = entry.getValue();
            data = new byte[headItem.length];
            System.arraycopy(dexData, headItem.start, data, 0, headItem.length);
            headItem.data = data;
        }
    }

    public void fillData() {
        Set<Map.Entry<String, DexHeadItem>> entries = headers.entrySet();
        for (Map.Entry<String, DexHeadItem> entry : entries) {
            DexHeadItem item = entry.getValue();
            if (item.hasSubContent()) {
                String desc = item.getContentDesc();
                DexDataItem dataItem = dataItems.get(desc);
                if (dataItem == null) {
                    dataItem = new DexDataItem(desc);
                    dataItems.put(desc, dataItem);
                }
                if (item.type == DexItem.TYPE_INT_SIZE) {
                    dataItem.length = item.bytes2Int();
                }
                if (item.type == DexItem.TYPE_INT_OFFSET) {
                    dataItem.start = item.bytes2Int();
                }
                if (dataItem.readyToFill()) {
                    byte[] data = new byte[dataItem.length];
                    System.arraycopy(dexData, dataItem.start, data, 0, dataItem.length);
                    dataItem.data = data;
                }
            }
        }
    }

}
