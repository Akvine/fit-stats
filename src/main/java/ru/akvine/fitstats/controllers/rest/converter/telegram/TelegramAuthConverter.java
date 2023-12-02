package ru.akvine.fitstats.controllers.rest.converter.telegram;

import com.google.common.base.Preconditions;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.rest.dto.telegram.GenerateAuthCodeResponse;
import ru.akvine.fitstats.services.dto.telegram.TelegramAuthCode;

@Component
public class TelegramAuthConverter {
    public GenerateAuthCodeResponse convertToGenerateAuthCodeResponse(TelegramAuthCode telegramAuthCode) {
        Preconditions.checkNotNull(telegramAuthCode, "telegramAuthCode is null");

        return new GenerateAuthCodeResponse()
                .setCode(telegramAuthCode.getCode())
                .setCreatedDate(telegramAuthCode.getCreatedDate().toString())
                .setExpiredAt(telegramAuthCode.getExpiredAt().toString());
    }
}
