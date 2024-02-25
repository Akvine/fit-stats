package ru.akvine.fitstats.controllers.rest.dto.barcode;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class AddBarCodeRequest {
    @NotBlank
    private String number;
    @NotBlank
    private String productUuid;
    private String type;
}
