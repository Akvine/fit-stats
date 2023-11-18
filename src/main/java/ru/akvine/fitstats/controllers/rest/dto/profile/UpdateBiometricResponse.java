package ru.akvine.fitstats.controllers.rest.dto.profile;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.rest.dto.common.SuccessfulResponse;

@Data
@Accessors(chain = true)
public class UpdateBiometricResponse extends SuccessfulResponse {
    private int age;

    private String height;

    private String weight;

    private String physicalActivity;

    private String heightMeasurement;

    private String weightMeasurement;
}
