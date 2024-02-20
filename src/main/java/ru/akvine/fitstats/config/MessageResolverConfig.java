package ru.akvine.fitstats.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import ru.akvine.fitstats.enums.Language;
import ru.akvine.fitstats.services.MessageResolveService;
import ru.akvine.fitstats.services.properties.PropertyService;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class MessageResolverConfig {
    private final PropertyService propertyService;

    @Value("default.encoding")
    private String defaultEncodingPropertyName;
    @Value("localization.basename.messages.path")
    private String localizationMessagesPathPropertyName;


    @Bean
    public MessageResolveService messageResolveService() {
        String defaultEncoding = propertyService.getRequired(defaultEncodingPropertyName);
        String messagesPath = propertyService.getRequired(localizationMessagesPathPropertyName);

        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames(messagesPath);
        messageSource.setDefaultEncoding(defaultEncoding);

        Map<Language, Locale> locales = new HashMap<>();
        Language[] languages = Language.values();
        for (Language language : languages) {
            locales.put(language, new Locale(language.name().toLowerCase()));
        }
        return new MessageResolveService(messageSource, locales);
    }
}
