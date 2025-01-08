package com.openvector.dbvectorex.annotation;

import com.openvector.modelcore.enums.ModelType;
import io.github.javpower.vectorexcore.entity.DataType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xgc
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GenerationVector {

    /**
     * 要转换的字段
     */
    String to_name() default "";

    /**
     * 数据类型，默认为 FLOAT_VECTOR
     *
     * @see DataType
     */
    ModelType modelType() default ModelType.FACE_EMBEDDING;


}