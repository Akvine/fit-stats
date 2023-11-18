package ru.akvine.fitstats.services.dto.diet;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.enums.VolumeMeasurement;

@Data
@Accessors(chain = true)
public class AddDietRecordFinish {
    private String uuid;
    private String productUuid;
    private String productTitle;
    private VolumeMeasurement volumeMeasurement;
    private double proteins;
    private double fats;
    private double carbohydrates;
    private double calories;
    private double volume;
}
