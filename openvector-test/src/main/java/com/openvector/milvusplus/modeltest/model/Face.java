package com.openvector.milvusplus.modeltest.model;

import com.openvector.dbvectorex.annotation.GenerationVector;
import com.openvector.modelcore.enums.ModelType;
import io.github.javpower.vectorex.keynote.model.MetricType;
import io.github.javpower.vectorexcore.annotation.VectoRexCollection;
import io.github.javpower.vectorexcore.annotation.VectoRexField;
import io.github.javpower.vectorexcore.entity.DataType;
import lombok.Data;

import java.util.List;

@VectoRexCollection(name = "face")
@Data
public class Face {
    @VectoRexField(isPrimaryKey = true)
    private Long id;
    @VectoRexField
    private String name;
    @VectoRexField
    @GenerationVector(to_name = "imageVector",
            dataType = com.openvector.modelcore.enums.DataType.IMAGE,
            modelType= ModelType.FACE_EMBEDDING)
    private String image;

    @VectoRexField(dataType = DataType.FloatVector,
            dimension = 128,
            metricType = MetricType.FLOAT_COSINE_DISTANCE)
    private List<Float> imageVector;

}
