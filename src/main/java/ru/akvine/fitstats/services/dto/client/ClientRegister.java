package ru.akvine.fitstats.services.dto.client;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import ru.akvine.fitstats.enums.*;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class ClientRegister {
    private String email;
    private String password;
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
}
