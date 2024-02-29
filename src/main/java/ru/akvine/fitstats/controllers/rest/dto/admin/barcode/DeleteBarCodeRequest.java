package ru.akvine.fitstats.controllers.rest.dto.admin.barcode;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.rest.dto.admin.SecretRequest;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class DeleteBarCodeRequest extends SecretRequest {
    @NotBlank
    private String productUuid;
    @NotBlank
    private String number;
}
