package ru.akvine.fitstats.controllers.telegram.dto.product;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.telegram.dto.common.TelegramBaseRequest;

@Data
@Accessors(chain = true)
public class TelegramProductGetByBarCodeRequest extends TelegramBaseRequest {
    private byte[] photo;
}
