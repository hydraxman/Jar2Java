package com.bryansharp.jar2java.entity;

import com.bryansharp.jar2java.Utils;
import com.bryansharp.jar2java.entity.base.DexData;
import com.bryansharp.jar2java.entity.base.DexDataItem;
import com.bryansharp.jar2java.entity.data.FieldContent;
import com.bryansharp.jar2java.entity.data.MethodContent;
import com.bryansharp.jar2java.entity.data.ProtoContent;
import com.bryansharp.jar2java.entity.refs.FieldRef;
import com.bryansharp.jar2java.entity.refs.ProtoRef;

import java.util.Map;

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
        return FieldRef.SIZE;
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

    @Override
    public void parse2ndRealData(Map<String, DexDataItem> dataItems, byte[] dexData) {
        TypeIdsItem tItem = (TypeIdsItem) dataItems.get(DexData.TYPE_IDS);
        StringIdsItem sItem = (StringIdsItem) dataItems.get(DexData.STRING_IDS);
        this.realData = new FieldContent[refs.length];
        for (int i = 0; i < refs.length; i++) {
            FieldContent fieldContent = new FieldContent();
            fieldContent.className = tItem.realData[refs[i].classIdx];
            fieldContent.fieldtype = tItem.realData[refs[i].typeIdx];
            fieldContent.name = sItem.realData[refs[i].nameIdx];
            this.realData[i] = fieldContent;
        }
    }
}
