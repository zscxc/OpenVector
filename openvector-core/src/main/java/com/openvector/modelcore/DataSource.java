package com.openvector.modelcore;

import com.openvector.modelcore.enums.DataType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;

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
     * 数据类型
     * 枚举：TEXT, IMAGE, AUDIO, VIDEO, VECTOR
     */
    private DataType dataType;

    /**
     * 实际数据内容
     */
    private String content;

    public DataSource(String valueString) {
        this.content = valueString;
        this.sourceType = detectSourceType(valueString);
        this.dataType = DataType.TEXT;
    }

    private SourceType detectSourceType(String valueString) {
        if (valueString.startsWith("http")) return SourceType.URL;
        if (valueString.startsWith("data:")) return SourceType.BASE64;
        return SourceType.FILE;
    }

    // 支持显式指定 DataType 的构造方法
    public DataSource(String content, DataType dataType) {
        this.content = content;
        this.sourceType = detectSourceType(content);
        this.dataType = dataType;
    }
    
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
