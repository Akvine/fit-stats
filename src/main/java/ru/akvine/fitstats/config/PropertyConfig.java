package ru.akvine.fitstats.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import ru.akvine.fitstats.enums.PropertyLoadStrategyType;
import ru.akvine.fitstats.services.integration.ConfigaIntegrationService;
import ru.akvine.fitstats.services.properties.ApplicationConfigPropertyService;
import ru.akvine.fitstats.services.properties.ExternalAppPropertyService;
import ru.akvine.fitstats.services.properties.PropertyService;

@Configuration
public class PropertyConfig {
    @Value("${properties.load.strategy.type}")
    private String propertiesLoadStrategyType;

    @Bean
    public PropertyService propertyService(Environment environment,
                                           ConfigaIntegrationService configaIntegrationService) {
        PropertyLoadStrategyType type = PropertyLoadStrategyType.valueOf(propertiesLoadStrategyType.toUpperCase());
        switch (type) {
            case CONFIG_FILE:
                return new ApplicationConfigPropertyService(environment);
            case EXTERNAL_APP:
                return new ExternalAppPropertyService(configaIntegrationService);
            default:
                throw new IllegalStateException("Properties load service with strategy type = [" + propertiesLoadStrategyType + "] not supported!");
        }
    }
}
