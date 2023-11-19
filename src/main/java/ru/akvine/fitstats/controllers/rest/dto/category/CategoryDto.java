package ru.akvine.fitstats.controllers.rest.dto.category;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class CategoryDto {
    @NotBlank
    private String title;
}
