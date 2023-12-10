package ru.akvine.fitstats.controllers.telegram.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class TelegramBaseRequest {
    protected String clientUuid;
    protected String chatId;
    protected Long telegramId;
}
