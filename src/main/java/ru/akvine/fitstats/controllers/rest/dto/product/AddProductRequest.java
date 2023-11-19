package ru.akvine.fitstats.controllers.rest.dto.product;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

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

    @NotBlank
    private String volumeMeasurement;

    @NotNull
    private Set<String> categories;
}
