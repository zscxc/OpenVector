## OpenVector



### model-parent

统一版本，发布中央仓库

### vector-db

向量数据库集成模块，并可基于注解内嵌model的实现

- milvus(可使用MilvusPlus)
- redis
- Faiss
- 等等

### model-stater

基于model-core的核心实现

配置文件、自定义的注解、工具类等方面 简化core的使用

### model-core

`model-core`是系统的核心组件，负责定义和实现数据向量化的统一接口，以及协调模型提供者的调用。

1. **接口层（Interface Layer）**：
    - **VectorizeData**：定义一个核心接口，用于处理外部请求，将非结构化数据转换为向量数据。
    - **入参**：
        - **DataSource**：数据来源，可以是URL、文件路径（Path）或Base64编码的字符串。
        - **ModelType**：选择的模型类型，通过枚举定义，支持不同的模型版本。
        - **DataType**：数据类型，包括文本、图片、音频等，可能扩展到视频、PDF等。
    - **出参**：
        - **VectorData**：转换后的向量数据，以`List<Float>`形式返回。
    - **异常处理**：明确定义可能抛出的异常类型，如`DataSourceNotFoundException`、`UnsupportedDataTypeException`等。

2. **数据来源处理（DataSourceHandler）**：
    - **描述**：根据不同的数据来源类型，实现具体的数据读取逻辑。
    - **URL处理**：处理网络请求。
    - **Path处理**：处理文件系统路径。
    - **Base64处理**：处理Base64编码的数据。

3. **模型协调层（Model Coordinator）**：
    - **描述**：负责根据请求中的`ModelType`和`DataType`选择合适的模型提供者，并调度模型的调用。
    - **模型选择**：根据请求参数选择最合适的模型。
    - **结果聚合**：聚合模型提供者返回的向量数据，并进行必要的后处理。

### model-provider

`model-provider`是一组模块化的模型实现，每个模块都遵循`model-core`定义的接口规范。

1. **模型适配层（Model Adapter Layer）**：

    - **描述**：每个模型实现都包装在一个适配器中，适配器确保模型的输入输出符合`model-core`的要求。

2. **模块化设计（Modular Design）**：

    - **模块化**：每个模块独立

    - **开源与第三方集成**：支持开源模型和第三方模型的集成。

    - **统一接口**：所有模型实现相同的接口，确保一致性和互换性。



###  model-test

内部测试用