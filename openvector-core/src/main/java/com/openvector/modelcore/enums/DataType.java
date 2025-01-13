package com.openvector.modelcore.enums;

import lombok.Getter;

/**
 * @author cxc
 */
@Getter
public enum DataType {
    TEXT("文本"),
    IMAGE("图片"),
    AUDIO("音频"),
    VIDEO("视频"),
    PDF("PDF文档");

    private final String description;

    DataType(String description) {
        this.description = description;
    }

}
