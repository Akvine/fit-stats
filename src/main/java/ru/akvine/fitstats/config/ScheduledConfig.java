package ru.akvine.fitstats.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.akvine.fitstats.job.RefreshPropertiesJob;
import ru.akvine.fitstats.services.properties.ExternalAppPropertyService;

@Configuration
@EnableScheduling
public class ScheduledConfig {

    @ConditionalOnBean(ExternalAppPropertyService.class)
    public RefreshPropertiesJob refreshPropertiesJob(ExternalAppPropertyService externalAppPropertyService) {
        return new RefreshPropertiesJob(externalAppPropertyService);
    }
}
