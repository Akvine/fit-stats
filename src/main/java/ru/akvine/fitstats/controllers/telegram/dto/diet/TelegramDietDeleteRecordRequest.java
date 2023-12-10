package ru.akvine.fitstats.controllers.telegram.dto.diet;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.telegram.dto.common.TelegramBaseRequest;

@Data
@Accessors(chain = true)
public class TelegramDietDeleteRecordRequest extends TelegramBaseRequest {
    private String uuid;

    public TelegramDietDeleteRecordRequest(String uuid, String clientUuid, String chatId) {
        this.uuid = uuid;
        this.clientUuid = clientUuid;
        this.chatId = chatId;
    }
}
