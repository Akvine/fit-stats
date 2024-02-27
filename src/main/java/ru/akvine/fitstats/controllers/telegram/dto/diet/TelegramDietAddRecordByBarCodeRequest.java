package ru.akvine.fitstats.controllers.telegram.dto.diet;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.telegram.dto.common.TelegramBaseRequest;

@Data
@Accessors(chain = true)
public class TelegramDietAddRecordByBarCodeRequest extends TelegramBaseRequest {
    private double volume;
    private byte[] photo;
}
