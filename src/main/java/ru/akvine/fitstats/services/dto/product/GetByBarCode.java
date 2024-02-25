package ru.akvine.fitstats.services.dto.product;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GetByBarCode {
    private String clientUuid;
    private String number;
}
