package com.openvector.modelcore.coordinator;

import com.openvector.modelcore.DataSource;
import com.openvector.modelcore.enums.DataType;
import com.openvector.modelcore.enums.ModelType;
import com.openvector.modelcore.exception.DataProcessingException;
import com.openvector.modelcore.interfaces.ModelProvider;
import com.openvector.modelcore.interfaces.VectorizeData;

import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

/**
 * 模型协调器
 * 负责选择和调用合适的模型提供者
 *
 * @author cxc
 */
public class ModelCoordinator implements VectorizeData {

    @Override
    public List<Float> vectorize(DataSource source, ModelType modelType) throws DataProcessingException {
        // 使用Java SPI机制加载模型提供者
        ServiceLoader<ModelProvider> loader = ServiceLoader.load(ModelProvider.class);

        // 找到支持特定模型和数据类型的提供者
        ModelProvider selectedProvider = StreamSupport.stream(loader.spliterator(), false)
            .filter(provider -> provider.supports(modelType, detectDataType(source)))
            .findFirst()
            .orElseThrow(() -> new DataProcessingException("No suitable model provider found"));

        // 调用选中的模型提供者处理数据
        return selectedProvider.process(source);
    }

    /**
     * 检测数据源的类型
     * @param source 数据源
     * @return 数据类型
     */
    private DataType detectDataType(DataSource source) {
        // 实现数据类型检测逻辑
        // 可以根据文件扩展名、内容等进行推断
        return DataType.TEXT; // 示例默认返回文本类型
    }
}
