package ru.akvine.fitstats.controllers.rest.dto.product;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class ProductDto {
    @NotBlank
    private String uuid;

    @NotBlank
    private String title;

    @NotBlank
    private String producer;

    private double proteins;

    private double fats;

    private double carbohydrates;

    private double vol;

    private double calories;

    private double volume;

    private String measurement;
}
