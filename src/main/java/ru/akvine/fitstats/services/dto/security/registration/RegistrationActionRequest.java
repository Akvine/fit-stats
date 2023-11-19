package ru.akvine.fitstats.services.dto.security.registration;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import ru.akvine.fitstats.enums.*;

@Data
@Accessors(chain = true)
public class RegistrationActionRequest {
    private String sessionId;
    private String login;
    private String firstName;
    private String secondName;
    @Nullable
    private String thirdName;
    private int age;
    private String height;
    private String weight;
    private HeightMeasurement heightMeasurement;
    private WeightMeasurement weightMeasurement;
    private Gender gender;
    private PhysicalActivity physicalActivity;
    private Diet diet;

    @Nullable
    private String otp;
    private String password;
}
