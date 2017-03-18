package com.bryansharp.jar2java.entity;

import com.bryansharp.jar2java.Utils;
import com.bryansharp.jar2java.entity.base.DexData;
import com.bryansharp.jar2java.entity.base.DexDataItem;
import com.bryansharp.jar2java.entity.data.ClassContent;
import com.bryansharp.jar2java.entity.refs.ClassRef;

import java.util.Map;

/**
 * Created by bsp on 17/3/18.
 */
public class ClassDefsItem extends DexDataItem<ClassRef, ClassContent> {
    public ClassDefsItem(String name) {
        super(name);
    }

    @Override
    protected ClassRef[] createRefs() {
        return new ClassRef[count];
    }

    @Override
    protected int getRefSize() {
        return ClassRef.SIZE;
    }

    @Override
    protected ClassRef parseRef(int i) {
        ClassRef classRef = new ClassRef();
        classRef.classIdx = Utils.bytesToInt(data, i);
        i += 4;
        classRef.accessFlags = Utils.bytesToInt(data, i);
        i += 4;
        classRef.superclassIdx = Utils.bytesToInt(data, i);
        i += 4;
        classRef.interfacesOff = Utils.bytesToInt(data, i);
        i += 4;
        classRef.sourceFileIdx = Utils.bytesToInt(data, i);
        i += 4;
        classRef.annotationsOff = Utils.bytesToInt(data, i);
        i += 4;
        classRef.classDataOff = Utils.bytesToInt(data, i);
        i += 4;
        classRef.staticValuesOff = Utils.bytesToInt(data, i);
        return classRef;
    }

    @Override
    public void parse2ndRealData(Map<String, DexDataItem> dataItems, byte[] dexData) {
        TypeIdsItem item = (TypeIdsItem) dataItems.get(DexData.TYPE_IDS);
        StringIdsItem sItem = (StringIdsItem) dataItems.get(DexData.STRING_IDS);
        String[] realData = item.realData;
        this.realData = new ClassContent[refs.length];
        for (int i = 0; i < refs.length; i++) {
            ClassContent classContent = new ClassContent();
            classContent.className = realData[refs[i].classIdx];
            classContent.superClassName = realData[refs[i].superclassIdx];
            classContent.sourceFile = sItem.realData[refs[i].sourceFileIdx];
            this.realData[i] = classContent;
        }
    }

}
