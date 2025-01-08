package com.openvector.dbvectorex.handler;

import com.openvector.dbvectorex.annotation.GenerationVector;
import com.openvector.dbvectorex.wrapper.OpenVectorWrapper;
import com.openvector.modelcore.DataSource;
import com.openvector.modelcore.coordinator.ModelCoordinator;
import io.github.javpower.vectorexbootstater.core.VectoRexResult;
import io.github.javpower.vectorexbootstater.mapper.BaseVectoRexMapper;
import io.github.javpower.vectorexbootstater.util.VectorRexSpringUtils;
import io.github.javpower.vectorexcore.VectoRexClient;
import io.github.javpower.vectorexcore.annotation.VectoRexCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class OpenVectorService<T>  {
    private static final Logger logger = LoggerFactory.getLogger(OpenVectorService.class);
    // 缓存反射信息，提高性能
    private static final Map<Class<?>, List<VectorGenerationMetadata>> VECTOR_GENERATION_CACHE = new ConcurrentHashMap<>();

    // 向量生成元数据
    private static class VectorGenerationMetadata {
        Field sourceField;
        Field targetField;
        GenerationVector annotation;

        VectorGenerationMetadata(Field sourceField, Field targetField, GenerationVector annotation) {
            this.sourceField = sourceField;
            this.targetField = targetField;
            this.annotation = annotation;
        }
    }

    private final BaseVectoRexMapper<T> baseVectoRexMapper;

    private final ModelCoordinator modelCoordinator;

    public OpenVectorService(Class<T> entityType) {
        this(entityType, new ModelCoordinator());
    }

    public OpenVectorService(Class<T> entityType, ModelCoordinator modelCoordinator) {
        this.modelCoordinator = modelCoordinator;
        this.baseVectoRexMapper = createBaseMapper(entityType);
    }

    private BaseVectoRexMapper<T> createBaseMapper(Class<T> entityType) {
        return new BaseVectoRexMapper<T>() {
            public VectoRexClient getClient() {
                return VectorRexSpringUtils.getBean(VectoRexClient.class);
            }

            public Class<T> getEntityType() {
                return entityType;
            }

            public OpenVectorWrapper<T> queryWrapper() {
                Class<T> entityType = getEntityType();
                VectoRexCollection collectionAnnotation = entityType.getAnnotation(VectoRexCollection.class);
                return new OpenVectorWrapper<>(getClient(), collectionAnnotation.name(), entityType);
            }
        };
    }

    public void insert(T entity) {
        //todo 判断是否有GenerationVector注解，有的话，自动嵌入向量数据到指定字段
        // 处理向量生成
        T processedEntity = processVectorGeneration(entity);
        baseVectoRexMapper.insert(processedEntity);
    }
    public void insert(Collection<T> entities) {
        //todo 判断是否有GenerationVector注解，有的话，自动嵌入向量数据到指定字段
        Collection<T> processedEntities = processVectorGenerations(entities);
        baseVectoRexMapper.insert(processedEntities);
    }
    public void updateById(T entity) {
        //todo 判断是否有GenerationVector注解，有的话，自动嵌入向量数据到指定字段
        T processedEntity = processVectorGeneration(entity);
        baseVectoRexMapper.updateById(processedEntity);
    }
    public void updateById(Collection<T> entities) {
        //todo 判断是否有GenerationVector注解，有的话，自动嵌入向量数据到指定字段
        Collection<T> processedEntities = processVectorGenerations(entities);
        baseVectoRexMapper.updateById(processedEntities);
    }
    public void removeById(Serializable id) {
        baseVectoRexMapper.removeById(id);
    }
    public VectoRexResult getById(Serializable id) {
        return baseVectoRexMapper.getById(id);
    }
    public OpenVectorWrapper<T> queryWrapper() {
        return (OpenVectorWrapper<T>) baseVectoRexMapper.queryWrapper();
    }


    /**
     * 获取类的向量生成元数据（使用缓存）
     * @param entityClass 实体类
     * @return 向量生成元数据列表
     */
    private List<VectorGenerationMetadata> getVectorGenerationMetadata(Class<T> entityClass) {
        return VECTOR_GENERATION_CACHE.computeIfAbsent(entityClass, this::computeVectorGenerationMetadata);
    }


    /**
     * 计算向量生成元数据
     *
     * @param entityClass 实体类
     * @return 向量生成元数据列表
     */
    private List<VectorGenerationMetadata> computeVectorGenerationMetadata(Class<?> entityClass) {
        List<VectorGenerationMetadata> metadata = new ArrayList<>();

        for (Field sourceField : entityClass.getDeclaredFields()) {
            GenerationVector annotation = sourceField.getAnnotation(GenerationVector.class);
            if (annotation != null) {
                try {
                    // 确定目标字段名
                    String targetFieldName = determineTargetFieldName(sourceField, annotation);

                    // 查找目标字段
                    Field targetField = entityClass.getDeclaredField(targetFieldName);

                    // 设置可访问性
                    sourceField.setAccessible(true);
                    targetField.setAccessible(true);

                    metadata.add(new VectorGenerationMetadata(sourceField, targetField, annotation));
                } catch (NoSuchFieldException e) {
                    logger.warn("No target field found for {}", sourceField.getName(), e);
                }
            }
        }

        return metadata;
    }


    /**
     * 确定目标字段名
     *
     * @param sourceField 源字段
     * @param annotation 注解
     * @return 目标字段名
     */
    private String determineTargetFieldName(Field sourceField, GenerationVector annotation) {
        return annotation.to_name().isEmpty()
            ? sourceField.getName() + "Vector"
            : annotation.to_name();
    }


     /**
     * 处理单个实体的向量生成
      *
     * @param entity 实体对象
     * @return 处理后的实体
     */
    private T processVectorGeneration(T entity) {
        // 获取类的向量生成元数据
        List<VectorGenerationMetadata> metadata = getVectorGenerationMetadata((Class<T>) entity.getClass());

        for (VectorGenerationMetadata vectorMeta : metadata) {
            try {
                // 获取源字段值
                Object sourceValue = vectorMeta.sourceField.get(entity);
                if (sourceValue != null) {
                    // 生成向量
                    List<Float> embedding = generateEmbedding(sourceValue, vectorMeta.annotation);

                    // 设置目标字段
                    vectorMeta.targetField.set(entity, embedding);
                }
            } catch (Exception e) {
                logger.error("Vector generation failed for field {}",
                    vectorMeta.sourceField.getName(), e);
            }
        }

        return entity;
    }

    /**
     * 生成向量
     *
     * @param sourceValue 源值
     * @param annotation 注解
     * @return 向量
     */
    private List<Float> generateEmbedding(Object sourceValue, GenerationVector annotation) {
        try {
            return modelCoordinator.vectorize(
                new DataSource(sourceValue.toString()),
                annotation.modelType()
            );
        } catch (Exception e) {
            logger.error("Embedding generation failed", e);
            // 返回空向量或抛出异常，取决于具体需求
            return Collections.emptyList();
        }
    }


    /**
     * 批量处理向量生成
     *
     * @param entities 实体集合
     * @return 处理后的实体集合
     */
    private Collection<T> processVectorGenerations(Collection<T> entities) {
        return entities.stream()
            .map(this::processVectorGeneration)
            .collect(Collectors.toList());
    }

}
