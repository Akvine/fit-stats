package ru.akvine.fitstats.services.dto.diet;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class UpdateDietSetting {
    private String clientUuid;
    private double maxProteins;
    private double maxFats;
    private double maxCarbohydrates;
    private double maxCalories;
}
