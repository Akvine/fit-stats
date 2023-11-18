package ru.akvine.fitstats.controllers.rest.dto.admin;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Accessors(chain = true)
public class UpdateProductRequest extends SecretRequest {
    @NotBlank
    private String uuid;

    private String title;

    private String producer;

    private Double proteins;

    private Double fats;

    private Double carbohydrates;

    private Double volume;

    private String measurement;
}
