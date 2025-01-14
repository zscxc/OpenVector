package com.openvector.dbmilvusplus.wrapper;


import com.openvector.dbmilvusplus.handler.OpenVectorService;
import org.dromara.milvus.plus.core.FieldFunction;
import org.dromara.milvus.plus.core.conditions.LambdaQueryWrapper;

import java.util.List;

import static com.openvector.dbmilvusplus.handler.OpenVectorService.VECTOR_GENERATION_CACHE;


public class OpenVectorWrapper<T> extends LambdaQueryWrapper {

    public OpenVectorWrapper() {
        super();
    }
    public OpenVectorWrapper<T> vector(String annsField, String sourceData) {
        super.setAnnsField(annsField);
        List<OpenVectorService.VectorGenerationMetadata> vectorGenerationMetadata = VECTOR_GENERATION_CACHE.get(super.getEntityType());
        OpenVectorService.VectorGenerationMetadata metadata = vectorGenerationMetadata.stream().filter(v -> v.getTargetField().getName().equals(annsField)).findFirst().get();
        List<Float> vector = metadata.generateEmbedding(sourceData);
        super.vector(annsField,vector);
        return this;
    }
    public OpenVectorWrapper<T> vector(FieldFunction<T,?> annsField, String sourceData) {
        String fieldName = annsField.getFieldName(annsField);
        return vector(fieldName, sourceData);
    }

    @Override
    public OpenVectorWrapper<T> wrapper() {
        return this;
    }

}

