package ru.akvine.fitstats.controllers.rest.dto.profile;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateBiometricRequest {
    private Integer age;
    private String weight;
    private String height;
    private String physicalActivity;
    private String diet;
    private boolean updateDietSetting;
}
