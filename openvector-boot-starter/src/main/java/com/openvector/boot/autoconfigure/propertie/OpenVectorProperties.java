package com.openvector.boot.autoconfigure.propertie;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cxc
 */
@Data
@ConfigurationProperties(prefix = "openvector")
public class OpenVectorProperties {

    private List<String> scanPackages = new ArrayList<>();

    // 可以添加更多全局配置
    private Map<String, Object> vectorDb = new HashMap<>();
}
