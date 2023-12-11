package ru.akvine.fitstats.controllers.telegram.dto.statistic;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.telegram.dto.common.TelegramBaseRequest;

@Data
@Accessors(chain = true)
public class TelegramStatisticHistoryRequest extends TelegramBaseRequest {
    private String duration;
    private String macronutrient;
}
