package ru.akvine.fitstats.controllers.telegram.dto.product;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.telegram.dto.common.TelegramBaseRequest;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class TelegramProductListRequest extends TelegramBaseRequest {
    private String filter;

    public TelegramProductListRequest(String clientUuid, String chatId, String filter) {
        this.clientUuid = clientUuid;
        this.chatId = chatId;
        this.filter = filter;
    }
}
