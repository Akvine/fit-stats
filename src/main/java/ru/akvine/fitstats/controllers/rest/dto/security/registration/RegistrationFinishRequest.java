package ru.akvine.fitstats.controllers.rest.dto.security.registration;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.akvine.fitstats.controllers.rest.dto.security.LoginRequest;

import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Data
public class RegistrationFinishRequest extends LoginRequest {
    @NotBlank
    @ToString.Exclude
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String secondName;

    @NotBlank
    private String thirdName;

    private int age;

    @NotBlank
    private String weight;

    @NotBlank
    private String height;

    @NotBlank
    private String heightMeasurement;

    @NotBlank
    private String weightMeasurement;

    @NotBlank
    private String gender;

    @NotBlank
    private String physicalActivity;

    @NotBlank
    private String diet;
}
