package ru.akvine.fitstats.controllers.rest.dto.barcode;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.rest.dto.common.SuccessfulResponse;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Accessors(chain = true)
public class ListBarCodeResponse extends SuccessfulResponse {
    @NotNull
    @Valid
    private List<BarCodeDto> barCodes;
}
