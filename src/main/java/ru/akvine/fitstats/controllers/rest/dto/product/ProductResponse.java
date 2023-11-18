package ru.akvine.fitstats.controllers.rest.dto.product;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.rest.dto.common.SuccessfulResponse;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class ProductResponse extends SuccessfulResponse {
    @Valid
    @NotNull
    private ProductDto product;
}
