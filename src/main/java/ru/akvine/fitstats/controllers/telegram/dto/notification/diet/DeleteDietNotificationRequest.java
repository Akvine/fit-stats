package ru.akvine.fitstats.controllers.telegram.dto.notification.diet;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.telegram.dto.common.TelegramBaseRequest;

@Data
@Accessors(chain = true)
public class DeleteDietNotificationRequest extends TelegramBaseRequest {
    private String type;

    public DeleteDietNotificationRequest(String clientUuid, String email, String chatId, Long telegramId, String type) {
        super(clientUuid, email, chatId, telegramId);
        this.type = type;
    }
}
