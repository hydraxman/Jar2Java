package com.bryansharp.jar2java.entity;

/**
 * Created by bushaopeng on 17/3/16.
 */
public class DexItem {
    public static final int TYPE_INT_SIZE = 1;
    public static final int TYPE_INT_OFFSET = 2;
    public static final int TYPE_INT_INFO = 3;
    public static final int TYPE_INT_DATA = 4;
    public String name;
    public int type;
    public int start = -1;
    public int length = -1;
    public byte[] data;
    public String desc;

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

    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序。 和bytesToInt（）配套使用
     *
     * @param value 要转换的int值
     * @return byte数组
     */
    public static byte[] intToBytes(int value) {
        byte[] src = new byte[4];
        src[3] = (byte) ((value >> 24) & 0xFF);
        src[2] = (byte) ((value >> 16) & 0xFF);
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序。  和bytesToInt2（）配套使用
     */
    public static byte[] intToBytes2(int value) {
        byte[] src = new byte[4];
        src[0] = (byte) ((value >> 24) & 0xFF);
        src[1] = (byte) ((value >> 16) & 0xFF);
        src[2] = (byte) ((value >> 8) & 0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序。
     *
     * @param ary    byte数组
     * @param offset 从数组的第offset位开始
     * @return int数值
     */
    public static int bytesToInt(byte[] ary, int offset) {
        int value;
        value = (int) ((ary[offset] & 0xFF)
                | ((ary[offset + 1] << 8) & 0xFF00)
                | ((ary[offset + 2] << 16) & 0xFF0000)
                | ((ary[offset + 3] << 24) & 0xFF000000));
        return value;
    }
}
