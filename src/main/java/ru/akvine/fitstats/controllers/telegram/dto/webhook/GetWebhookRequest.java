package ru.akvine.fitstats.controllers.telegram.dto.webhook;

import lombok.Getter;

@Getter
public class GetWebhookRequest {
    private static final String TELEGRAM_REQUEST_METHOD = "/getWebhookInfo";

    private final String telegramRequestMethod;

    public GetWebhookRequest(String telegramApiUrl, String botToken) {
        this.telegramRequestMethod = telegramApiUrl + botToken + TELEGRAM_REQUEST_METHOD;
    }
}
