package ru.akvine.fitstats.services.dto.barcode;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DeleteBarCode {
    private String productUuid;
    private String clientUuid;
    private String number;
}
