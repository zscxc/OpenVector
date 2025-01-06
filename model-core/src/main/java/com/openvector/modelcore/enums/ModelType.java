package com.openvector.modelcore.enums;

/**
 * @author cxc
 */
public enum ModelType {

    OPENAI_EMBEDDING("OpenAI"),
    BERT_BASE("BERT"),
    CUSTOM("Custom"),
    FACE_EMBEDDING("人脸嵌入");  // 新增人脸嵌入类型;

    private final String description;

    ModelType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
