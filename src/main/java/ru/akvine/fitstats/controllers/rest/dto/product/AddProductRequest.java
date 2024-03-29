package ru.akvine.fitstats.controllers.rest.dto.product;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class AddProductRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String producer;

    private double proteins;

    private double fats;

    private double carbohydrates;

    private Double alcohol;

    private Double vol;

    @NotBlank
    private String volumeMeasurement;
}
