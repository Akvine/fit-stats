package ru.akvine.fitstats.services.dto.barcode;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ListBarCode {
    private String clientUuid;
    private String productUuid;
}
