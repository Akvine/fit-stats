package ru.akvine.fitstats.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.akvine.commons.cluster.keepalive.KeepAliveService;
import ru.akvine.fitstats.services.properties.PropertyParseService;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class KeepAliveConfig {
    private final PropertyParseService propertyParseService;

    @Value("db.keepalive.interval.seconds")
    private String dbKeepAliveIntervalSecondsPropertyName;
    @Value("db.keepalive.delay.coefficient")
    private String dbKeepAliveDelayCoefficientPropertyName;

    @Bean(initMethod = "start", destroyMethod = "destroy")
    public KeepAliveService keepAliveService(DataSource dataSource) {
        long dbKeepAliveIntervalSeconds = propertyParseService.parseLong(dbKeepAliveIntervalSecondsPropertyName);
        int dbKeepAliveDelayCoefficient = propertyParseService.parseInteger(dbKeepAliveDelayCoefficientPropertyName);
        return new KeepAliveService(dataSource, dbKeepAliveIntervalSeconds, dbKeepAliveDelayCoefficient);
    }
}
