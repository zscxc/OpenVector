package com.openvector.dbvectorex.handler;

import com.openvector.dbvectorex.wrapper.OpenVectorWrapper;
import io.github.javpower.vectorexbootstater.core.VectoRexResult;
import io.github.javpower.vectorexbootstater.mapper.BaseVectoRexMapper;
import io.github.javpower.vectorexbootstater.util.VectorRexSpringUtils;
import io.github.javpower.vectorexcore.VectoRexClient;
import io.github.javpower.vectorexcore.annotation.VectoRexCollection;

import java.io.Serializable;
import java.util.Collection;

public class OpenVectorService<T>  {
    private BaseVectoRexMapper<T> baseVectoRexMapper;
    public OpenVectorService(Class<T> entityType) {
        baseVectoRexMapper= new BaseVectoRexMapper<T>(){
            public VectoRexClient getClient() {
                return VectorRexSpringUtils.getBean(VectoRexClient.class);
            }
            public Class<T> getEntityType(){
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
        baseVectoRexMapper.insert(entity);
    }
    public void insert(Collection<T> entities) {
        //todo 判断是否有GenerationVector注解，有的话，自动嵌入向量数据到指定字段

        baseVectoRexMapper.insert(entities);
    }
    public void updateById(T entity) {
        //todo 判断是否有GenerationVector注解，有的话，自动嵌入向量数据到指定字段
        baseVectoRexMapper.updateById(entity);
    }
    public void updateById(Collection<T> entities) {
        //todo 判断是否有GenerationVector注解，有的话，自动嵌入向量数据到指定字段
        baseVectoRexMapper.updateById(entities);
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


}
