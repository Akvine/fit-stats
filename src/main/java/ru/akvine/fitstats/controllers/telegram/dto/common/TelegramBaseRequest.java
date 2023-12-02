package ru.akvine.fitstats.controllers.telegram.dto.common;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors
public abstract class TelegramBaseRequest {
    protected String clientUuid;
    protected String chatId;
}
