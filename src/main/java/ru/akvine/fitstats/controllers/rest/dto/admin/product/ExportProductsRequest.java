package ru.akvine.fitstats.controllers.rest.dto.admin.product;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.controllers.rest.dto.admin.SecretRequest;

@Data
@Accessors(chain = true)
public class ExportProductsRequest extends SecretRequest {
    private String converterType;
    private String filename;
}
