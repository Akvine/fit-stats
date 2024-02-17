package ru.akvine.fitstats.controllers.telegram.validators;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.services.properties.PropertyParseService;

@Component
@Slf4j
@RequiredArgsConstructor
public class TelegramBotSecretValidator {
    @Value("telegram.bot.secret")
    private String botSecretPropertyName;

    private final PropertyParseService propertyParseService;

    public void verifyBotPathSecret(String botSecret) {
        String actualBotSecret = propertyParseService.get(botSecretPropertyName);

        if (StringUtils.isBlank(botSecret) || !StringUtils.equals(botSecret, actualBotSecret)) {
            logger.warn("Received invalid telegram webhook bot secret=[{}]", botSecret);
            throw new IllegalArgumentException("Invalid telegram webhook bot secret");
        }
    }
}
