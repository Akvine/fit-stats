package ru.akvine.fitstats.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.akvine.fitstats.enums.NotificationProviderType;
import ru.akvine.fitstats.services.notification.NotificationProvider;
import ru.akvine.fitstats.services.notification.dummy.DummyNotificationProvider;
import ru.akvine.fitstats.services.notification.dummy.LogNotificationService;
import ru.akvine.fitstats.services.properties.PropertyParseService;
import ru.akvine.fitstats.validators.NotificationValidator;

import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Configuration
public class NotificationConfig {
    private final PropertyParseService propertyParseService;

    @Value("security.dummy.notification.provider.enabled")
    private String dummyProviderEnabled;
    @Value("security.notification.provider.type")
    private String notificationProviderType;

    private final Map<NotificationProviderType, NotificationProvider> availableNotificationProviders;
    private final NotificationValidator notificationValidator;

    @Autowired
    public NotificationConfig(List<NotificationProvider> notificationProviders,
                              NotificationValidator notificationValidator,
                              PropertyParseService propertyParseService) {
        this.notificationValidator = notificationValidator;
        this.propertyParseService = propertyParseService;
        this.availableNotificationProviders = notificationProviders
                .stream()
                .collect(toMap(NotificationProvider::getType, identity()));
    }

    @Bean
    public NotificationProvider notificationProvider() {
        String providerType = propertyParseService.get(notificationProviderType);
        notificationValidator.validate(providerType);
        NotificationProviderType type = NotificationProviderType.valueOf(providerType);

        NotificationProvider notificationProvider = availableNotificationProviders.get(type);
        boolean isDummyProviderEnabled = propertyParseService.parseBoolean(dummyProviderEnabled);
        if (isDummyProviderEnabled && notificationProvider instanceof DummyNotificationProvider) {
            return notificationProvider;
        }
        if (!isDummyProviderEnabled && notificationProvider instanceof DummyNotificationProvider) {
            throw new IllegalStateException("Dummy notification provider can't be used in non-testing environment!");
        }

        if (isDummyProviderEnabled && !(notificationProvider instanceof DummyNotificationProvider)) {
            throw new IllegalStateException("Real notification provider can't be used in testing environment!");
        }

        return new LogNotificationService();
    }
}
