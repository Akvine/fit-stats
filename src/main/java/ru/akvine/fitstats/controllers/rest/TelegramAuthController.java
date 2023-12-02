package ru.akvine.fitstats.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.fitstats.controllers.rest.converter.telegram.TelegramAuthConverter;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.meta.telegram.TelegramAuthControllerMeta;
import ru.akvine.fitstats.services.dto.telegram.TelegramAuthCode;
import ru.akvine.fitstats.services.telegram.TelegramAuthService;
import ru.akvine.fitstats.utils.SecurityUtils;

@RestController
@RequiredArgsConstructor
public class TelegramAuthController implements TelegramAuthControllerMeta {
    private final TelegramAuthService telegramAuthService;
    private final TelegramAuthConverter telegramAuthConverter;

    @Override
    public Response generate() {
        String clientUuid = SecurityUtils.getCurrentUser().getUuid();
        TelegramAuthCode telegramAuthCode = telegramAuthService.generate(clientUuid);
        return telegramAuthConverter.convertToGenerateAuthCodeResponse(telegramAuthCode);
    }

    @Override
    public Response get() {
        String clientUuid = SecurityUtils.getCurrentUser().getUuid();
        TelegramAuthCode telegramAuthCode = telegramAuthService.getByClientUuid(clientUuid);
        return telegramAuthConverter.convertToGenerateAuthCodeResponse(telegramAuthCode);
    }
}
