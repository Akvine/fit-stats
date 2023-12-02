package ru.akvine.fitstats.controllers.telegram.dto.diet;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.telegram.dto.common.TelegramBaseRequest;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class TelegramDietAddRecord extends TelegramBaseRequest {
    private String text;

    public TelegramDietAddRecord(String clientUuid, String chatId, String text) {
        this.clientUuid = clientUuid;
        this.chatId = chatId;
        this.text = text;
    }
}
