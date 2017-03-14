package com.bryansharp.jar2java;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bushaopeng on 17/3/14.
 */
public class DexData {
    private List<DexHeadItem> headers;

    public DexData() {
        headers = new ArrayList<>();
        int start = 0;
        headers.add(new DexHeadItem("magic", start, 8));
        start += 8;
        headers.add(new DexHeadItem("checksum", start, 4));
        start += 0x14;
        headers.add(new DexHeadItem("signature", start, 0x14));
        start += 4;
        headers.add(new DexHeadItem("fileSize", start, 4));
        start += 4;
        headers.add(new DexHeadItem("headerSize", start, 4));
        start += 4;
        headers.add(new DexHeadItem("endianTag", start, 4));
        start += 4;
        headers.add(new DexHeadItem("linkSize", start, 4));
        start += 4;
        headers.add(new DexHeadItem("linkOff", start, 4));
        start += 4;
        headers.add(new DexHeadItem("mapOff", start, 4));
        start += 4;
        headers.add(new DexHeadItem("stringIdsSize", start, 4));
        start += 4;
        headers.add(new DexHeadItem("stringIdsOff", start, 4));
        start += 4;
        headers.add(new DexHeadItem("typeIdsSize", start, 4));
        start += 4;
        headers.add(new DexHeadItem("typeIdsOff", start, 4));
        start += 4;
        headers.add(new DexHeadItem("protoIdsSize", start, 4));
        start += 4;
        headers.add(new DexHeadItem("protoIdsOff", start, 4));
        start += 4;
        headers.add(new DexHeadItem("fieldIdsSize", start, 4));
        start += 4;
        headers.add(new DexHeadItem("fieldIdsOff", start, 4));
        start += 4;
        headers.add(new DexHeadItem("methodIdsSize", start, 4));
        start += 4;
        headers.add(new DexHeadItem("methodIdsOff", start, 4));
        start += 4;
        headers.add(new DexHeadItem("classDefsSize", start, 4));
        start += 4;
        headers.add(new DexHeadItem("classDefsOff", start, 4));
        start += 4;
        headers.add(new DexHeadItem("dataSize", start, 4));
        start += 4;
        headers.add(new DexHeadItem("dataOff", start, 4));
        int len = start + 4;
        LogUtils.log("header总长度：" + len);
    }

    public void fillData(InputStream inputStream) {
        byte[] data = null;
        for (DexHeadItem header : headers) {
            data = new byte[header.length];
            try {
                inputStream.read(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
            header.data = data;
        }
    }

    public static class DexHeadItem {
        public String name;
        public int start;
        public int length;
        public byte[] data;
        public String desc;

        public DexHeadItem(String name, int start, int length) {
            this.name = name;
            this.start = start;
            this.length = length;
        }

        @Override
        public String toString() {
            return name + ":" + bytes2Int();
        }

        public int bytes2Int() {
            int mask = 0xff;
            if (length == 4) {
                int temp = 0;
                int n = 0;
                temp = data[3] & mask;
                n |= temp;
                n = n << 8;
                temp = data[2] & mask;
                n |= temp;
                n = n << 8;
                temp = data[1] & mask;
                n |= temp;
                n = n << 8;
                temp = data[0] & mask;
                n |= temp;
                return n;
            }
            return -1;
        }
    }
}
