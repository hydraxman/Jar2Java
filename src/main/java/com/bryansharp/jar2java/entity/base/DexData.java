package com.bryansharp.jar2java.entity.base;


import com.bryansharp.jar2java.LogUtils;
import com.bryansharp.jar2java.entity.ByteItem;
import com.bryansharp.jar2java.entity.ClassDefsItem;
import com.bryansharp.jar2java.entity.FieldIdsItem;
import com.bryansharp.jar2java.entity.IntRefsItem;
import com.bryansharp.jar2java.entity.MethodIdsItem;
import com.bryansharp.jar2java.entity.ProtoIdsItem;
import com.bryansharp.jar2java.entity.StringIdsItem;
import com.bryansharp.jar2java.entity.TypeIdsItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by bushaopeng on 17/3/14.
 */
public class DexData {
    public static final String STRING_IDS = "stringIds";
    public static final String CLASS_DEFS = "classDefs";
    public static final String TYPE_IDS = "typeIds";
    public static final String METHOD_IDS = "methodIds";
    public static final String PROTO_IDS = "protoIds";
    public static final String FIELD_IDS = "fieldIds";
    private static final String[] sizeIsCountNames = new String[]{
            STRING_IDS, CLASS_DEFS, TYPE_IDS, METHOD_IDS, PROTO_IDS, FIELD_IDS
    };
    public static final List<String> sizeIsCountNamesArr = Arrays.asList(sizeIsCountNames);
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
            data = new byte[headItem.byteSize];
            System.arraycopy(dexData, headItem.start, data, 0, headItem.byteSize);
            headItem.data = data;
        }
    }

    public void fillData() {
        Set<Map.Entry<String, DexHeadItem>> entries = headers.entrySet();
        DexDataItem dataItem;
        //生成数据条目对象
        for (Map.Entry<String, DexHeadItem> entry : entries) {
            DexHeadItem item = entry.getValue();
            if (item.hasSubContent()) {
                String desc = item.getContentDesc();
                dataItem = dataItems.get(desc);
                if (dataItem == null) {
                    dataItem = getDexDataItem(desc);
                    dataItems.put(desc, dataItem);
                }
                if (item.type == DexItem.TYPE_INT_SIZE) {
                    dataItem.count = item.bytes2Int();
                    dataItem.byteSize = dataItem.count * dataItem.getRefSize();
                    dataItem.refs = dataItem.createRefs();
                }
                if (item.type == DexItem.TYPE_INT_OFFSET) {
                    dataItem.start = item.bytes2Int();
                }
            }
        }
        for (Map.Entry<String, DexDataItem> entry : dataItems.entrySet()) {
            dataItem = entry.getValue();
            if (dataItem.readyToFill()) {
                dataItem.fillData(dexData);
                dataItem.fillRefs();
                dataItem.parseBaseRealData(dexData);
            }
        }
        for (Map.Entry<String, DexDataItem> entry : dataItems.entrySet()) {
            dataItem = entry.getValue();
            if (dataItem.readyToFill()) {
                dataItem.parse1stRealData(dataItems, dexData);
            }
        }
        for (Map.Entry<String, DexDataItem> entry : dataItems.entrySet()) {
            dataItem = entry.getValue();
            if (dataItem.readyToFill()) {
                dataItem.parse2ndRealData(dataItems, dexData);
            }
        }
        for (Map.Entry<String, DexDataItem> entry : dataItems.entrySet()) {
            dataItem = entry.getValue();
            if (dataItem.readyToFill()) {
                dataItem.parse3rdRealData(dataItems, dexData);
            }
        }
        for (Map.Entry<String, DexDataItem> entry : dataItems.entrySet()) {
            dataItem = entry.getValue();
            if (dataItem.readyToFill()) {
                dataItem.parse4thRealData(dataItems, dexData);
            }
        }
    }

    private DexDataItem getDexDataItem(String desc) {
        DexDataItem dataItem;
        if (DexData.STRING_IDS.equals(desc)) {
            dataItem = new StringIdsItem(desc);
        } else if (DexData.TYPE_IDS.equals(desc)) {
            dataItem = new TypeIdsItem(desc);
        } else if (DexData.CLASS_DEFS.equals(desc)) {
            dataItem = new ClassDefsItem(desc);
        } else if (DexData.PROTO_IDS.equals(desc)) {
            dataItem = new ProtoIdsItem(desc);
        } else if (DexData.METHOD_IDS.equals(desc)) {
            dataItem = new MethodIdsItem(desc);
        } else if (DexData.FIELD_IDS.equals(desc)) {
            dataItem = new FieldIdsItem(desc);
        } else {
            dataItem = new ByteItem(desc);
        }
        return dataItem;
    }

    public void printData() {
        for (Map.Entry<String, DexDataItem> entry : dataItems.entrySet()) {
            DexDataItem item = entry.getValue();
            if (item != null) {
                item.printData();
            }
        }
    }
}
