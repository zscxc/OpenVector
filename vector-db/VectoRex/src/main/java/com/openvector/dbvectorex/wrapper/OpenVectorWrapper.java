package com.openvector.dbvectorex.wrapper;

import io.github.javpower.vectorexbootstater.builder.LambdaQueryWrapper;
import io.github.javpower.vectorexbootstater.core.FieldFunction;
import io.github.javpower.vectorexcore.VectoRexClient;

public class OpenVectorWrapper<T> extends LambdaQueryWrapper<T> {

    public OpenVectorWrapper(VectoRexClient client, String collectionName, Class<T> entityType) {
        super(client, collectionName, entityType);
    }

    public LambdaQueryWrapper<T> vector(String annsField, String floatVector) {
        super.setAnnsField(annsField);
        //todo floatVector to list
        super.setVector(null);
        return this;
    }
    public LambdaQueryWrapper<T> vector(FieldFunction<T,?> annsField, String floatVector) {
        super.setAnnsField(annsField.getFieldName(annsField));
        //todo floatVector to list
        super.setVector(null);
        return this;
    }

}

