package ru.akvine.fitstats.controllers.rest.dto.admin;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ExportProductsRequest extends SecretRequest {
    private String converterType;
    private String filename;
}
