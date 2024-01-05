package ru.akvine.fitstats.controllers.rest.dto.diet;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class ChangeDietRequest {
    @NotBlank
    private String dietType;
}
