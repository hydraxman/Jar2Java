package com.bryansharp.jar2java.entity;

import com.bryansharp.jar2java.Utils;
import com.bryansharp.jar2java.entity.base.DexData;
import com.bryansharp.jar2java.entity.base.DexDataItem;
import com.bryansharp.jar2java.entity.data.ClassContent;
import com.bryansharp.jar2java.entity.data.ProtoContent;
import com.bryansharp.jar2java.entity.refs.ClassRef;
import com.bryansharp.jar2java.entity.refs.ProtoRef;

import java.util.Map;

/**
 * Created by bsp on 17/3/18.
 */
public class ProtoIdsItem extends DexDataItem<ProtoRef, ProtoContent> {
    public ProtoIdsItem(String name) {
        super(name);
    }

    @Override
    protected ProtoRef[] createRefs() {
        return new ProtoRef[count];
    }

    @Override
    protected int getRefSize() {
        return ProtoRef.SIZE;
    }

    @Override
    protected ProtoRef parseRef(int i) {
        ProtoRef protoRef = new ProtoRef();
        protoRef.shortyIdx = Utils.bytesToInt(data, i);
        i += 4;
        protoRef.returnTypeIdx = Utils.bytesToInt(data, i);
        i += 4;
        protoRef.parametersOff = Utils.bytesToInt(data, i);
        return protoRef;
    }

    @Override
    public void parse2ndRealData(Map<String, DexDataItem> dataItems, byte[] dexData) {
        TypeIdsItem item = (TypeIdsItem) dataItems.get(DexData.TYPE_IDS);
        StringIdsItem sItem = (StringIdsItem) dataItems.get(DexData.STRING_IDS);
        String[] realData = item.realData;
        this.realData = new ProtoContent[refs.length];
        for (int i = 0; i < refs.length; i++) {
            ProtoContent protoContent = new ProtoContent();
            protoContent.shorty = sItem.realData[refs[i].shortyIdx];
            protoContent.returnType = realData[refs[i].returnTypeIdx];
            this.realData[i] = protoContent;
        }
    }
}
