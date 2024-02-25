package ru.akvine.fitstats.controllers.rest.dto.product;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class GetByBarcodeNumberRequest {
    @NotBlank
    private String number;
}
