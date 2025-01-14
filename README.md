# OpenVector: 开源的多模型集成平台
![logo](logo.jpg)

## 🚀 项目简介

`OpenVector` 是一个创新的开源 Java 库，旨在简化机器学习模型的集成与使用。我们提供了一个统一且强大的接口，让开发者能够轻松整合来自不同来源的预训练嵌入模型。

## 🎯 项目愿景

在快速发展的 AI 领域，获取和使用高质量的预训练模型至关重要。OpenVector 致力于解决模型使用中的技术障碍，让开发者可以专注于创新应用的构建。

## ✨ 核心特性

- **🌐 广泛的模型支持**
  - 无缝集成 Hugging Face、TensorFlow Hub、PyTorch Hub、阿里云等平台的预训练模型
  - 支持多种数据类型：文本、图片、音频等

- **🔧 简便的注解机制**
  - 通过简单的注解配置，快速指定和切换模型
  - 极大地简化模型管理流程

- **🔄 一致的 API 接口**
  - 统一的调用方式，确保代码可移植性
  - 屏蔽不同模型间的技术差异

- **⚡ 高性能优化**
  - 深度性能优化
  - 支持数据处理流水线
  - GPU 加速
  - 智能资源管理

## 🛠️ 快速开始

### 环境准备

- JDK 1.8+
- Maven
- CMake（可选，用于构建动态库）

### 安装依赖

```shell
1、构建动态库文件
$ cd jdlib/jni
$ mkdir build
$ cd build
$ cmake ..
$ make 
2、打包 face-dlib项目
2、打包 face-dlib项目
```

```xml
<!-- 暂未上传至 maven ，提供本地依赖示例 -->
<dependency>
    <groupId>com.openvector</groupId>
    <artifactId>openvector-core</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 基本使用示例

```java
@Autowired
private VectorizeService vectorizeService;

// 使用注解快速调用模型
@VectorModel(type = ModelType.TEXT_EMBEDDING)
public List<Float> embedText(String text) {
    return vectorizeService.vectorize(text);
}
```

### 📦 模块说明

- `openvector-core`：系统核心组件，定义向量化统一接口
- `openvector-db`：向量数据库集成模块
- `openvector-provider`：模型提供者实现

## 贡献指南

1. Fork 项目
2. 创建 Feature 分支 (`git checkout -b feature/AmazingFeature`)
3. 提交代码 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

## 📄 许可证

本项目基于 Apache 2.0 许可证开源 - 详见 [LICENSE](LICENSE) 文件

## 支持与联系

- GitHub 主页: [https://github.com/zscxc/OpenVector](https://github.com/zscxc/OpenVector)
- Gitee 主页: [https://gitee.com/cencxc/open-vector](https://gitee.com/cencxc/open-vector)
- GitHub Issues: [提交问题](https://gitee.com/cencxc/open-vector/issues)
- 邮箱: 320522275@qq.com

## 赞助与支持

如果你觉得这个项目对你有帮助，欢迎 Star ⭐ 本项目！

## 友情连接
- [VectoRex](https://gitee.com/giteeClass/VectoRex)
- [MilvusPlus](https://gitee.com/dromara/MilvusPlus)
