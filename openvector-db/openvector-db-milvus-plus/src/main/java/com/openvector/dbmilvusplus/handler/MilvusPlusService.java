package com.openvector.dbmilvusplus.handler;

import com.openvector.dbmilvusplus.annotaion.GenerationVector;
import com.openvector.modelcore.DataSource;
import com.openvector.modelcore.coordinator.ModelCoordinator;
import com.openvector.modelcore.enums.DataType;
import com.openvector.modelcore.exception.DataProcessingException;
import io.milvus.v2.service.vector.response.DeleteResp;
import io.milvus.v2.service.vector.response.InsertResp;
import io.milvus.v2.service.vector.response.UpsertResp;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dromara.milvus.plus.core.conditions.LambdaQueryWrapper;
import org.dromara.milvus.plus.core.mapper.BaseMilvusMapper;
import org.dromara.milvus.plus.model.vo.MilvusResp;
import org.dromara.milvus.plus.model.vo.MilvusResult;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author cxc
 */
@Slf4j
public class MilvusPlusService<T> {

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

    private final BaseMilvusMapper<T> baseMilvusMapper;
    private final ModelCoordinator modelCoordinator;

    public MilvusPlusService(BaseMilvusMapper<T> baseMilvusMapper) {
        this(baseMilvusMapper, new ModelCoordinator());
    }

    public MilvusPlusService(BaseMilvusMapper<T> baseMilvusMapper, ModelCoordinator modelCoordinator) {
        this.baseMilvusMapper = baseMilvusMapper;
        this.modelCoordinator = modelCoordinator;
    }

    public MilvusResp<InsertResp> insert(T entity) throws DataProcessingException {
        T processedEntity = processVectorGeneration(entity);
        return baseMilvusMapper.insert(processedEntity);
    }

    public MilvusResp<InsertResp> insert(Collection<T> entities) {
        Collection<T> processedEntities = processVectorGenerations(entities);
        return baseMilvusMapper.insert(processedEntities);
    }

    public MilvusResp<UpsertResp> updateById(T entity) throws DataProcessingException {
        T processedEntity = processVectorGeneration(entity);
        return baseMilvusMapper.updateById(processedEntity);
    }

    public MilvusResp<UpsertResp> updateById(Collection<T> entities) {
        Collection<T> processedEntities = processVectorGenerations(entities);
        return baseMilvusMapper.updateById(processedEntities);
    }

    public MilvusResp<DeleteResp> removeById(Serializable id) {
        return baseMilvusMapper.removeById(id);
    }

    public MilvusResp<List<MilvusResult<T>>> getById(Serializable id) {
        return baseMilvusMapper.getById(id);
    }

    public LambdaQueryWrapper<T> queryWrapper() {
        return baseMilvusMapper.queryWrapper();
    }

    public MilvusResp<List<MilvusResult<T>>> query(Consumer<LambdaQueryWrapper<T>> consumer) {
        LambdaQueryWrapper<T> wrapper = queryWrapper();
        consumer.accept(wrapper);
        return wrapper.query();
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
                    log.warn("No target field found for {}", sourceField.getName(), e);
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
    private T processVectorGeneration(T entity) throws DataProcessingException {
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
                log.error("Vector generation failed for field {}",
                    vectorMeta.sourceField.getName(), e);
                throw new DataProcessingException("Vector generation failed", e);
            }
        }

        return entity;
    }

    /**
     * 批量处理实体的向量生成
     *
     * @param entities 实体集合
     * @return 处理后的实体集合
     */
    @SneakyThrows
    private Collection<T> processVectorGenerations(Collection<T> entities) {
        return entities.stream()
            .map(this::processVectorGeneration)
            .collect(Collectors.toList());
    }

    /**
     * 生成向量
     *
     * @param sourceValue 源值
     * @param annotation 注解
     * @return 向量
     */
    private List<Float> generateEmbedding(Object sourceValue, GenerationVector annotation) throws DataProcessingException {
        try {
            DataSource dataSource;
            if (sourceValue == null) {
                throw new IllegalArgumentException("Source value cannot be null");
            }
            // 使用注解中指定的数据类型创建 DataSource
            dataSource = new DataSource(sourceValue.toString()) {
                @Override
                public DataType getDataType() {
                    return annotation.dataType();
                }
            };
            return modelCoordinator.vectorize(
                dataSource,
                annotation.modelType()
            );
        } catch (Exception e) {
            log.error("Embedding generation failed for value: {}, data type: {}, model type: {}",
            sourceValue, annotation.dataType(), annotation.modelType(), e);
            throw new DataProcessingException("Embedding generation failed", e);
        }
    }


}
