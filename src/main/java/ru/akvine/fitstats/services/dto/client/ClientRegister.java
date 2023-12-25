package ru.akvine.fitstats.services.dto.client;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.jetbrains.annotations.Nullable;
import ru.akvine.fitstats.enums.*;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class ClientRegister {
    private String email;
    @ToStringExclude
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
