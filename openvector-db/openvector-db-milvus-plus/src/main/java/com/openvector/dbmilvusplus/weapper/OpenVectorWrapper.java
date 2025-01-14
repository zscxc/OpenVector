package com.openvector.dbmilvusplus.weapper;

import com.openvector.dbmilvusplus.handler.MilvusPlusService;
import io.milvus.v2.client.MilvusClientV2;
import org.dromara.milvus.plus.cache.ConversionCache;
import org.dromara.milvus.plus.core.FieldFunction;
import org.dromara.milvus.plus.core.conditions.LambdaQueryWrapper;

import java.util.List;

import static com.openvector.dbmilvusplus.handler.MilvusPlusService.VECTOR_GENERATION_CACHE;

/**
 * @author cxc
 */
public class OpenVectorWrapper<T> extends LambdaQueryWrapper<T> {

    public OpenVectorWrapper(MilvusClientV2 clientV2, String collectionName, Class<T> entityType) {
        super.init(collectionName, clientV2, new ConversionCache(), entityType);
    }


    public LambdaQueryWrapper<T> vector(String annsField, String sourceData) {
        super.setAnnsField(annsField);
        List<MilvusPlusService.VectorGenerationMetadata> vectorGenerationMetadata = VECTOR_GENERATION_CACHE.get(super.getEntityType());
        MilvusPlusService.VectorGenerationMetadata metadata = vectorGenerationMetadata.stream()
                .filter(v -> v.getTargetField().getName().equals(annsField)).findFirst().get();
        List<Float> vector = metadata.generateEmbedding(sourceData);
        super.vector(vector);
        return this;
    }

    public LambdaQueryWrapper<T> vector(FieldFunction<T,?> annsField, String sourceData) {
        String fieldName = annsField.getFieldName(annsField);
       return vector(fieldName, sourceData);
    }
}
