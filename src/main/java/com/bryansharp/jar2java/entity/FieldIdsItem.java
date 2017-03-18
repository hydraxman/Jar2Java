package com.bryansharp.jar2java.entity;

import com.bryansharp.jar2java.Utils;
import com.bryansharp.jar2java.entity.base.DexDataItem;
import com.bryansharp.jar2java.entity.data.FieldContent;
import com.bryansharp.jar2java.entity.data.ProtoContent;
import com.bryansharp.jar2java.entity.refs.FieldRef;
import com.bryansharp.jar2java.entity.refs.ProtoRef;

/**
 * Created by bsp on 17/3/18.
 */
public class FieldIdsItem extends DexDataItem<FieldRef, FieldContent> {
    public FieldIdsItem(String name) {
        super(name);
    }

    @Override
    protected FieldRef[] createRefs() {
        return new FieldRef[count];
    }

    @Override
    protected int getRefSize() {
        return ProtoRef.SIZE;
    }

    @Override
    protected FieldRef parseRef(int i) {
        FieldRef fieldRef = new FieldRef();
        fieldRef.classIdx = Utils.u2ToInt(data, i);
        i += 2;
        fieldRef.typeIdx = Utils.u2ToInt(data, i);
        i += 2;
        fieldRef.nameIdx = Utils.bytesToInt(data, i);
        return fieldRef;
    }
}
