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
        this.dataType = detectDataType(valueString);
    }

    private SourceType detectSourceType(String valueString) {
        if (valueString.startsWith("http")) return SourceType.URL;
        if (valueString.startsWith("data:")) return SourceType.BASE64;
        return SourceType.FILE;
    }

    private DataType detectDataType(String valueString) {
        if (isImageFile(valueString)) return DataType.IMAGE;
        if (isTextFile(valueString)) return DataType.TEXT;
        return DataType.TEXT;
    }

    private boolean isImageFile(String valueString) {
        String[] imageExtensions = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp"};
        return Arrays.stream(imageExtensions)
            .anyMatch(valueString.toLowerCase()::endsWith);
    }

    private boolean isTextFile(String valueString) {
        String[] textExtensions = {".txt", ".md", ".csv", ".json", ".xml", ".html"};
        return Arrays.stream(textExtensions)
            .anyMatch(valueString.toLowerCase()::endsWith);
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
