package com.openvector.modelcore.util;

import com.openvector.modelcore.interfaces.ModelProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ProviderUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
    private static Map<String, ModelProvider> modelProviders;

    @Override
    public void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
        modelProviders = applicationContext.getBeansOfType(ModelProvider.class);
    }

    public static <T> T getBean(String name, Class<T> requiredType) {
        return applicationContext.getBean(name, requiredType);
    }

    public static Map<String, ModelProvider> getModelProviders() {
        if (modelProviders == null) {
            throw new IllegalStateException("SpringUtil has not been initialized. Ensure the application context is set.");
        }
        return modelProviders;
    }
}