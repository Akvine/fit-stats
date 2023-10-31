package ru.akvine.fitstats.services.dto.product;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import ru.akvine.fitstats.enums.VolumeMeasurement;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Accessors(chain = true)
public class UpdateProduct {
    @NotBlank
    private String uuid;

    @Nullable
    private String title;

    @Nullable
    private String producer;

    @Nullable
    private Double proteins;

    @Nullable
    private Double fats;

    @Nullable
    private Double carbohydrates;

    @Nullable
    private Double volume;

    @Nullable
    private VolumeMeasurement measurement;
}
