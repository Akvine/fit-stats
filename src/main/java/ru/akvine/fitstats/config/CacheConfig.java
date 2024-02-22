package ru.akvine.fitstats.config;

import com.google.common.base.Ticker;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.akvine.fitstats.cache.ClientSettingsCache;
import ru.akvine.fitstats.services.client.settings.CachableGetClientSettingsService;
import ru.akvine.fitstats.services.client.settings.ClientSettingsService;
import ru.akvine.fitstats.services.client.settings.DatabaseGetClientSettingsService;
import ru.akvine.fitstats.services.client.settings.GetClientSettingsService;
import ru.akvine.fitstats.services.dto.client.ClientSettingsBean;
import ru.akvine.fitstats.services.properties.PropertyParseService;

import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class CacheConfig {
    private final PropertyParseService propertyParseService;

    @Value("client.settings.cache.enabled")
    private String clientSettingsCacheEnabledPropertyName;
    @Value("client.settings.cache.expire.seconds")
    private String expireSecondsPropertyName;
    @Value("client.settings.cache.size.seconds")
    private String sizePropertyName;

    @Bean
    public DatabaseGetClientSettingsService databaseGetClientSettingsService(ClientSettingsService clientSettingsService) {
        return new DatabaseGetClientSettingsService(clientSettingsService);
    }

    @Bean
    public GetClientSettingsService getClientSettingsService(DatabaseGetClientSettingsService databaseGetClientSettingsService) {
        boolean isEnabled = propertyParseService.parseBoolean(clientSettingsCacheEnabledPropertyName);
        if (isEnabled) {
            return new CachableGetClientSettingsService(clientSettingsCache(databaseGetClientSettingsService));
        } else {
            return databaseGetClientSettingsService;
        }
    }

    @Bean
    public ClientSettingsCache clientSettingsCache(DatabaseGetClientSettingsService databaseGetClientSettingsService) {
        LoadingCache<String, ClientSettingsBean> settings = CacheBuilder
                .newBuilder()
                .expireAfterWrite(propertyParseService.parseLong(expireSecondsPropertyName), TimeUnit.SECONDS)
                .ticker(Ticker.systemTicker())
                .maximumSize(propertyParseService.parseLong(sizePropertyName))
                .build(
                        new CacheLoader<>() {
                            @Override
                            public ClientSettingsBean load(@NotNull String key) {
                                return databaseGetClientSettingsService.getByClientEmail(key);
                            }
                        }
                );
        return new ClientSettingsCache(settings);
    }
}
