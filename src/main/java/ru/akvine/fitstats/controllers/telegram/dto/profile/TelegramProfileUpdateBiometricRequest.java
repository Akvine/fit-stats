package ru.akvine.fitstats.controllers.telegram.dto.profile;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.telegram.dto.common.TelegramBaseRequest;

@Data
@Accessors(chain = true)
public class TelegramProfileUpdateBiometricRequest extends TelegramBaseRequest {
    private Integer age;
    private String weight;
    private String height;
    private String physicalActivity;
    private boolean updateDietSetting;
}
