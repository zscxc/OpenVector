package com.openvector.modelcore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author cxc
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataSource {
    /**
     * 唯一标识符
     */
    private String sourceId;

    /**
     * 数据源类型
     * 枚举：URL, FILE, BASE64, STREAM
     */
    private SourceType sourceType;

    /**
     * 实际数据内容
     */
    private String content;

    /**
     * 数据源类型枚举
     */
    public enum SourceType {
        URL,      // 网络资源
        FILE,     // 文件路径
        BASE64,   // Base64编码
        STREAM    // 数据流
    }
}
