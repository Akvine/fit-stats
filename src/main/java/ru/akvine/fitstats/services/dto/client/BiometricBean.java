package ru.akvine.fitstats.services.dto.client;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.entities.BiometricEntity;
import ru.akvine.fitstats.enums.Gender;
import ru.akvine.fitstats.enums.HeightMeasurement;
import ru.akvine.fitstats.enums.PhysicalActivity;
import ru.akvine.fitstats.enums.WeightMeasurement;
import ru.akvine.fitstats.services.dto.base.Bean;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class BiometricBean extends Bean {
    private Long id;
    private int age;
    private String height;
    private String weight;
    private HeightMeasurement heightMeasurement;
    private WeightMeasurement weightMeasurement;
    private Gender gender;
    private PhysicalActivity physicalActivity;
    private ClientBean clientBean;

    public BiometricBean(BiometricEntity biometricEntity) {
        this.id = biometricEntity.getId();
        this.age = biometricEntity.getAge();
        this.height = biometricEntity.getHeight();
        this.weight = biometricEntity.getWeight();
        this.gender = biometricEntity.getGender();
        this.heightMeasurement = biometricEntity.getHeightMeasurement();
        this.weightMeasurement = biometricEntity.getWeightMeasurement();
        this.physicalActivity = biometricEntity.getPhysicalActivity();
        this.clientBean = new ClientBean(biometricEntity.getClientEntity());
    }
}
