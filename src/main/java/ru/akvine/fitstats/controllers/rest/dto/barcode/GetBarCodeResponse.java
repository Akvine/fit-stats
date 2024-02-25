package ru.akvine.fitstats.controllers.rest.dto.barcode;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.rest.dto.common.SuccessfulResponse;

@Data
@Accessors(chain = true)
public class GetBarCodeResponse extends SuccessfulResponse {
    private BarCodeDto barCodeDto;
}
