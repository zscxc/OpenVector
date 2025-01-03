package com.openvector.modelcore.interfaces;

import com.openvector.modelcore.DataSource;
import com.openvector.modelcore.enums.DataType;
import com.openvector.modelcore.enums.ModelType;
import com.openvector.modelcore.exception.DataProcessingException;

import java.util.List;

/**
 * 模型提供者接口
 * 定义模型提供者的基本行为
 *
 * @author cxc
 */
public interface  ModelProvider {

    /**
     * 检查是否支持特定模型和数据类型
     * @param modelType 模型类型
     * @param dataType 数据类型
     * @return 是否支持
     */
    boolean supports(ModelType modelType, DataType dataType);

    /**
     * 处理数据源并生成向量
     * @param source 数据源
     * @return 向量数据
     * @throws DataProcessingException 处理异常
     */
    List<Float> process(DataSource source) throws DataProcessingException;

}
