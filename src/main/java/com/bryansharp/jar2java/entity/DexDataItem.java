package com.bryansharp.jar2java.entity;

/**
 * Created by bushaopeng on 17/3/17.
 */
public class DexDataItem extends DexItem {
    public DexDataItem(String name) {
        super();

    }

    public boolean readyToFill() {
        return length > 0 && start > 0;
    }
}
