package ru.akvine.fitstats.controllers.rest.dto.product;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Accessors(chain = true)
public class AddProductRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String producer;

    private double proteins;

    private double fats;

    private double carbohydrates;

    @NotBlank
    private String volumeMeasurement;
}
