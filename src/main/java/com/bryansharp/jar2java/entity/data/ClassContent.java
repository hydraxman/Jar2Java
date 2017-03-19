package com.bryansharp.jar2java.entity.data;

import com.bryansharp.jar2java.Mutf8;
import com.bryansharp.jar2java.Utils;
import com.bryansharp.jar2java.entity.FieldIdsItem;
import com.bryansharp.jar2java.entity.MethodIdsItem;
import com.bryansharp.jar2java.entity.base.DexData;
import com.bryansharp.jar2java.entity.base.DexDataItem;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by bsp on 17/3/18.
 */
public class ClassContent {
    public String className;
    public String superClassName;
    public String sourceFile;
    public ClassData classData;

    @Override
    public String toString() {
        return "\nClassContent{" +
                "className='" + className + '\'' +
                ", superClassName='" + superClassName + '\'' +
                ", sourceFile='" + sourceFile + '\'' +
                ", classData=" + classData +
                '}';
    }

    public static class ClassData {
        DexField[] staticFields;
        DexField[] instanceFields;
        DexMethod[] directMethods;
        DexMethod[] virtualMethods;
        public byte[] data;
        public int sizeOfSizes;

        public ClassData(int[] sizes) {
            this.staticFields = new DexField[sizes[0]];
            this.instanceFields = new DexField[sizes[1]];
            this.directMethods = new DexMethod[sizes[2]];
            this.virtualMethods = new DexMethod[sizes[3]];
        }

        public void fillData(Map<String, DexDataItem> dataItems, byte[] dexData) {
            FieldIdsItem fItem = (FieldIdsItem) dataItems.get(DexData.FIELD_IDS);
            MethodIdsItem mItem = (MethodIdsItem) dataItems.get(DexData.METHOD_IDS);
            int offset = fillFields(sizeOfSizes, staticFields, fItem);
            offset = fillFields(offset, instanceFields, fItem);
            offset = fillMethods(offset, directMethods, mItem, dexData);
            offset = fillMethods(offset, virtualMethods, mItem, dexData);
            if (data.length > offset) {
                byte[] bytes = new byte[offset];
                System.arraycopy(data, 0, bytes, 0, offset);
                this.data = bytes;
            }
        }

        private int fillFields(int offset, DexField[] fields, FieldIdsItem fItem) {
            int valueOffset = 0;
            for (int i = 0; i < fields.length; i++) {
                int[] result = Mutf8.readUnsignedLeb128(data, offset);
                valueOffset += result[0];
                fields[i] = new DexField();
                fields[i].field = fItem.realData[valueOffset];
                offset += result[1];
                result = Mutf8.readUnsignedLeb128(data, offset);
                fields[i].accessFlags = result[0];
                offset += result[1];
            }
            return offset;
        }

        private int fillMethods(int offset, DexMethod[] methods, MethodIdsItem mItem, byte[] dexData) {
            int valueOffset = 0;
            for (int i = 0; i < methods.length; i++) {
                int[] result = Mutf8.readUnsignedLeb128(data, offset);
                valueOffset += result[0];
                methods[i] = new DexMethod();
                methods[i].method = mItem.realData[valueOffset];
                offset += result[1];
                result = Mutf8.readUnsignedLeb128(data, offset);
                methods[i].accessFlags = result[0];
                offset += result[1];
                result = Mutf8.readUnsignedLeb128(data, offset);
                methods[i].codeOff = result[0];
                offset += result[1];
                int codeOff = methods[i].codeOff;
                DexCode code = methods[i].code;
                code.registersSize = Utils.u2ToInt(dexData, codeOff);
                codeOff += 2;
                code.insSize = Utils.u2ToInt(dexData, codeOff);
                codeOff += 2;
                code.outsSize = Utils.u2ToInt(dexData, codeOff);
                codeOff += 2;
                code.triesSize = Utils.u2ToInt(dexData, codeOff);
                codeOff += 2;
                code.debugInfoOff = Utils.bytesToInt(dexData, codeOff);
                codeOff += 4;
                code.insnsSize = Utils.bytesToInt(dexData, codeOff);
                codeOff += 4;
                code.insns = new int[code.insnsSize];
                code.insnsRaw = new byte[code.insnsSize * 2];
                System.arraycopy(dexData, codeOff, code.insnsRaw, 0, code.insnsSize * 2);
                for (int j = 0; j < code.insnsSize; j++) {
                    code.insns[j] = Utils.u2ToInt(dexData, codeOff);
                    codeOff += 2;
                }
                code.parseInsns();
            }
            return offset;
        }

        @Override
        public String toString() {
            return "\n\tClassData{" +
                    "\n\tstaticFields=" + Arrays.toString(staticFields) +
                    ", \n\tinstanceFields=" + Arrays.toString(instanceFields) +
                    ", \n\tdirectMethods=" + Arrays.toString(directMethods) +
                    ", \n\tvirtualMethods=" + Arrays.toString(virtualMethods) +
                    ", \n\tdata=" + Utils.bytesToString(data, 0) +
                    '}';
        }
    }
}
