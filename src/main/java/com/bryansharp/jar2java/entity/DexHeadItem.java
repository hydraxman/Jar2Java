package com.bryansharp.jar2java.entity;

/**
 * Created by bushaopeng on 17/3/16.
 */
public class DexHeadItem extends DexItem {


    public DexHeadItem(String name, int start, int length) {
        this.name = name;
        this.start = start;
        this.length = length;
        this.type = TYPE_INT_INFO;
    }

    public DexHeadItem(String name, int start, int length, int type) {
        this.name = name;
        this.start = start;
        this.length = length;
        this.type = type;
    }

    @Override
    public String toString() {
        return name + ":" + bytes2Int();
    }

    public String getContentDesc() {
        switch (type) {
            case TYPE_INT_SIZE:
                return name.replace("Size", "").replace("size", "");
            case TYPE_INT_OFFSET:
                return name.replace("Off", "").replace("off", "");
        }
        return "";
    }

    public boolean hasSubContent() {
        return type == TYPE_INT_OFFSET || type == TYPE_INT_SIZE;
    }
}
