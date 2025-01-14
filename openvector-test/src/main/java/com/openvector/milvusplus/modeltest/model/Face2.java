package com.openvector.milvusplus.modeltest.model;

import com.openvector.dbvectorex.annotation.GenerationVector;
import com.openvector.modelcore.enums.ModelType;
import io.milvus.v2.common.IndexParam;
import lombok.Data;
import org.dromara.milvus.plus.annotation.ExtraParam;
import org.dromara.milvus.plus.annotation.MilvusCollection;
import org.dromara.milvus.plus.annotation.MilvusField;
import org.dromara.milvus.plus.annotation.MilvusIndex;

import java.util.List;

@MilvusCollection(name = "face_openvector")
@Data
public class Face2 {
    @MilvusField(
            dataType = io.milvus.v2.common.DataType.Int64,
            isPrimaryKey = true // 标记为主键
    )
    private Long id;

    @MilvusField(dataType = io.milvus.v2.common.DataType.VarChar)
    private String name;

    @GenerationVector(to_name = "imageVector",
            dataType = com.openvector.modelcore.enums.DataType.IMAGE,
            modelType= ModelType.FACE_EMBEDDING)
    @MilvusField(dataType = io.milvus.v2.common.DataType.VarChar)
    private String image;


    @MilvusField(
            name = "imageVector", // 字段名称
            dataType = io.milvus.v2.common.DataType.FloatVector, // 数据类型为浮点型向量
            dimension = 128 // 向量维度，人脸特征向量的维度是128
    )

    @MilvusIndex(
            indexName = "imageVector_index", // 索引名称
            indexType = IndexParam.IndexType.IVF_FLAT, // 使用IVF_FLAT索引类型
            metricType = IndexParam.MetricType.L2, // 使用L2距离度量类型
            extraParams = { // 指定额外的索引参数
                    @ExtraParam(key = "nlist", value = "100"), // 例如，IVF_FLAT 的nlist参数
                    @ExtraParam(key = "nprobe", value = "20")
            }
    )
    private List<Float> imageVector;

}
