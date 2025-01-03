package com.openvector.modelcore.interfaces;

import com.openvector.modelcore.DataSource;
import com.openvector.modelcore.enums.ModelType;
import com.openvector.modelcore.exception.DataProcessingException;

import java.util.List;

/**
 * 向量化数据接口
 * 定义数据向量化的基本契约
 *
 * @author cxc
 */
public interface VectorizeData {

    /**
     * 将数据源转换为向量
     * @param source 数据源
     * @param modelType 模型类型
     * @return 向量数据
     * @throws DataProcessingException 数据处理异常
     */
    List<Float> vectorize(DataSource source, ModelType modelType) throws DataProcessingException;
}
