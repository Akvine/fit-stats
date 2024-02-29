package ru.akvine.fitstats.controllers.rest.dto.admin.product;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.rest.dto.admin.SecretRequest;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class UpdateProductRequest extends SecretRequest {
    @NotBlank
    private String uuid;

    private String title;

    private String producer;

    private Double proteins;

    private Double fats;

    private Double carbohydrates;

    private Double vol;

    private Double volume;

    private String measurement;
}
