package ru.akvine.fitstats.controllers.rest.dto.admin.barcode;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.rest.dto.common.SuccessfulResponse;

@Data
@Accessors(chain = true)
public class UpdateBarCodeResponse extends SuccessfulResponse {
    private String productUuid;
    private String number;
    private String type;
}
