package ru.akvine.fitstats.controllers.telegram.dto.diet;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.telegram.dto.common.TelegramBaseRequest;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class TelegramDietDisplayRequest extends TelegramBaseRequest {

    public TelegramDietDisplayRequest(String clientUuid, String chatId) {
        this.clientUuid = clientUuid;
        this.chatId = chatId;
    }
}
