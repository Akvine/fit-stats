package ru.akvine.fitstats.controllers.telegram.dto.notification.diet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.telegram.dto.common.TelegramBaseRequest;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class AddDietNotificationRequest extends TelegramBaseRequest {
    private String type;

    public AddDietNotificationRequest(String clientUuid,
                                      String chatId,
                                      Long telegramId,
                                      String type) {
        super(clientUuid, chatId, telegramId);
        this.type = type;
    }
}
