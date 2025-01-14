package com.openvector.dbvectorex.wrapper;

import com.openvector.dbvectorex.handler.OpenVectorService;
import io.github.javpower.vectorexbootstater.builder.LambdaQueryWrapper;
import io.github.javpower.vectorexbootstater.core.FieldFunction;
import io.github.javpower.vectorexcore.VectoRexClient;

import java.util.List;

import static com.openvector.dbvectorex.handler.OpenVectorService.VECTOR_GENERATION_CACHE;

public class OpenVectorWrapper<T> extends LambdaQueryWrapper<T> {

    public OpenVectorWrapper(VectoRexClient client, String collectionName, Class<T> entityType) {
        super(client, collectionName, entityType);
    }

    public LambdaQueryWrapper<T> vector(String annsField, String sourceData) {
        super.setAnnsField(annsField);
        List<OpenVectorService.VectorGenerationMetadata> vectorGenerationMetadata = VECTOR_GENERATION_CACHE.get(super.getEntityType());
        OpenVectorService.VectorGenerationMetadata metadata = vectorGenerationMetadata.stream().filter(v -> v.getTargetField().getName().equals(annsField)).findFirst().get();
        List<Float> vector = metadata.generateEmbedding(sourceData);
        super.setVector(vector);
        return this;
    }
    public LambdaQueryWrapper<T> vector(FieldFunction<T,?> annsField, String sourceData) {
        String fieldName = annsField.getFieldName(annsField);
       return vector(fieldName, sourceData);
    }

}

