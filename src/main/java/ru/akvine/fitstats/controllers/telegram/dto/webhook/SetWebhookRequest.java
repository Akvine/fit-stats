package ru.akvine.fitstats.controllers.telegram.dto.webhook;

import lombok.Getter;

import java.util.List;

@Getter
public class SetWebhookRequest {
    private static final String TELEGRAM_REQUEST_METHOD = "/setWebhook?url=";
    private static final String ALLOWED_UPDATED_PARAM = "&allowed_updates=";

    private String telegramRequestMethod;

    public SetWebhookRequest(String telegramApiUrl, String botToken, String botPath) {
        this.telegramRequestMethod = telegramApiUrl + botToken + TELEGRAM_REQUEST_METHOD + botPath;
    }

    public void setAllowedUpdates(List<String> allowedUpdates) {
        StringBuilder sb = new StringBuilder();
        sb.append(ALLOWED_UPDATED_PARAM);
        sb.append("[");
        allowedUpdates.forEach((update) -> {
            sb.append("\"");
            sb.append(update);
            sb.append("\"");
            sb.append(",");
        });
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");

        this.telegramRequestMethod += sb;
    }
}
