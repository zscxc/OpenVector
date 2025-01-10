package com.openvector.boot.autoconfigure;

import com.openvector.boot.autoconfigure.propertie.OpenVectorProperties;
import com.openvector.modelcore.coordinator.ModelCoordinator;
import com.openvector.modelcore.interfaces.ModelProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author cxc
 */
@Configuration
@EnableConfigurationProperties(OpenVectorProperties.class)
public class OpenVectorAutoConfiguration {

    private final OpenVectorProperties properties;
    private final ModelCoordinator modelCoordinator;

    public OpenVectorAutoConfiguration(
        OpenVectorProperties properties,
        ModelCoordinator modelCoordinator
    ) {
        this.properties = properties;
        this.modelCoordinator = modelCoordinator;
    }


    @Bean
    public ModelCoordinator modelCoordinator(List<ModelProvider> providers) {
        ModelCoordinator coordinator = new ModelCoordinator();
        // 注册所有 ModelProvider
//        providers.forEach(coordinator::registerProvider);
        return coordinator;
    }
}
