package com.openvector.modelcore.enums;

/**
 * @author cxc
 */
public enum ModelType {

    OPENAI_EMBEDDING("OpenAI"),
    BERT_BASE("BERT"),
    CUSTOM("Custom");

    private final String description;

    ModelType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
