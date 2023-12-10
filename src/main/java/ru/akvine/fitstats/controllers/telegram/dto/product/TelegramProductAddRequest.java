package ru.akvine.fitstats.controllers.telegram.dto.product;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.telegram.dto.common.TelegramBaseRequest;

import java.util.Set;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class TelegramProductAddRequest extends TelegramBaseRequest {
    private String title;
    private String producer;
    private double proteins;
    private double fats;
    private double carbohydrates;
    private double vol;
    private String volumeMeasurement;
    private Set<String> categories;
}
