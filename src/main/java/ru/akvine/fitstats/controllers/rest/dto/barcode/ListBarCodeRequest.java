package ru.akvine.fitstats.controllers.rest.dto.barcode;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class ListBarCodeRequest {
    @NotBlank
    private String productUuid;
}
