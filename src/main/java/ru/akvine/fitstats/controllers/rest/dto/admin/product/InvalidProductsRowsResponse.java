package ru.akvine.fitstats.controllers.rest.dto.admin.product;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.rest.dto.common.SuccessfulResponse;

import java.util.List;

@Data
@Accessors(chain = true)
public class InvalidProductsRowsResponse extends SuccessfulResponse {
    private List<InvalidProductRow> invalidRows;
}
