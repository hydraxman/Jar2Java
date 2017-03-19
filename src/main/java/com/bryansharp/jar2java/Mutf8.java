package com.bryansharp.jar2java;

import java.io.UTFDataFormatException;

/**
 * Modified UTF-8 as described in the dex file format spec.
 * <p/>
 * <p>Derived from libcore's MUTF-8 encoder at java.nio.charset.ModifiedUtf8.
 */
public final class Mutf8 {
    private Mutf8() {
    }

    /**
     * Decodes bytes from {@code in} into {@code out} until a delimiter 0x00 is
     * encountered. Returns a new string containing the decoded characters.
     */
    public static String decode(byte[] in) throws UTFDataFormatException {
        int s = 0;
        int[] sizeSpec = readUnsignedLeb128(in);
        int i = sizeSpec[1];
        char[] out = new char[sizeSpec[0]];
        while (true) {
            char a = (char) (in[i++] & 0xff);
            if (a == 0) {
                return new String(out, 0, s);
            }
            out[s] = a;
            if (a < '\u0080') {
                s++;
            } else if ((a & 0xe0) == 0xc0) {
                int b = in[i++] & 0xff;
                if ((b & 0xC0) != 0x80) {
                    throw new UTFDataFormatException("bad second byte");
                }
                out[s++] = (char) (((a & 0x1F) << 6) | (b & 0x3F));
            } else if ((a & 0xf0) == 0xe0) {
                int b = in[i++] & 0xff;
                int c = in[i++] & 0xff;
                if (((b & 0xC0) != 0x80) || ((c & 0xC0) != 0x80)) {
                    throw new UTFDataFormatException("bad second or third byte");
                }
                out[s++] = (char) (((a & 0x0F) << 12) | ((b & 0x3F) << 6) | (c & 0x3F));
            } else {
                throw new UTFDataFormatException("bad byte");
            }
        }
    }

    /**
     * mutf-8的前几位是长度！！
     *
     * @param in
     * @return
     */
    public static int[] readUnsignedLeb128(byte[] in) {
        return readUnsignedLeb128(in, 0);
    }

    /**
     * 每个leb128由1~5字节组成, 所有字节组合在一起表示一个32位的数据,
     * 每个字节只有7位有效, 如果第1个字节的最高位为1, 表示leb128需要使用到第2个字节,
     * 如果第2个字节的最高位为1, 表示需要使用到第3个字节, 以此类推直到最后的字节最高位为0,
     * 当然, leb128最多只会使用到5个字节, 如果读取5个字节后下一个字节最高位仍为1, 则表示该dex无效
     *
     * @param in
     * @return
     */
    public static int[] readUnsignedLeb128(byte[] in, int offset) {
        if (in == null) {
            return new int[]{0, 0};
        }
        int result = 0;
        int cur;
        int count = 0;
        int i = offset;
        do {
            cur = in[i++] & 0xff;
            result |= (cur & 0x7f) << (count * 7);
            count++;
        } while (((cur & 0x80) == 0x80) && count < 5);

        if ((cur & 0x80) == 0x80) {
        }

        return new int[]{result, count};
    }

    /**
     * Returns the number of bytes the modified UTF8 representation of 's' would take.
     */
    private static long countBytes(String s, boolean shortLength) throws UTFDataFormatException {
        long result = 0;
        final int length = s.length();
        for (int i = 0; i < length; ++i) {
            char ch = s.charAt(i);
            if (ch != 0 && ch <= 127) { // U+0000 uses two bytes.
                ++result;
            } else if (ch <= 2047) {
                result += 2;
            } else {
                result += 3;
            }
            if (shortLength && result > 65535) {
                throw new UTFDataFormatException("String more than 65535 UTF bytes long");
            }
        }
        return result;
    }

    /**
     * Encodes the modified UTF-8 bytes corresponding to {@code s} into  {@code
     * dst}, starting at {@code offset}.
     */
    public static void encode(byte[] dst, int offset, String s) {
        final int length = s.length();
        for (int i = 0; i < length; i++) {
            char ch = s.charAt(i);
            if (ch != 0 && ch <= 127) { // U+0000 uses two bytes.
                dst[offset++] = (byte) ch;
            } else if (ch <= 2047) {
                dst[offset++] = (byte) (0xc0 | (0x1f & (ch >> 6)));
                dst[offset++] = (byte) (0x80 | (0x3f & ch));
            } else {
                dst[offset++] = (byte) (0xe0 | (0x0f & (ch >> 12)));
                dst[offset++] = (byte) (0x80 | (0x3f & (ch >> 6)));
                dst[offset++] = (byte) (0x80 | (0x3f & ch));
            }
        }
    }

    /**
     * Returns an array containing the <i>modified UTF-8</i> form of {@code s}.
     */
    public static byte[] encode(String s) throws UTFDataFormatException {
        int utfCount = (int) countBytes(s, true);
        byte[] result = new byte[utfCount];
        encode(result, 0, s);
        return result;
    }
}
