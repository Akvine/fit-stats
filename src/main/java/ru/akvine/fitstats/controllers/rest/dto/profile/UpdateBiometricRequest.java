package ru.akvine.fitstats.controllers.rest.dto.profile;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class UpdateBiometricRequest {
    private Integer age;
    private String weight;
    private String height;
    private String physicalActivity;
    private String diet;
}
