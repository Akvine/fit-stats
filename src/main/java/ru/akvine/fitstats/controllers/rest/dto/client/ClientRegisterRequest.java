package ru.akvine.fitstats.controllers.rest.dto.client;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ClientRegisterRequest {
    private String email;
    private String password;
    private String firstName;
    private String secondName;
    private String thirdName;
    private int age;
    private String weight;
    private String height;
    private String heightMeasurement;
    private String weightMeasurement;
    private String gender;
    private String physicalActivity;
    private String diet;
}
