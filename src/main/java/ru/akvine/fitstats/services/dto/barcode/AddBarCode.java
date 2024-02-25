package ru.akvine.fitstats.services.dto.barcode;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import ru.akvine.fitstats.enums.BarCodeType;

@Data
@Accessors(chain = true)
public class AddBarCode {
    private String clientUuid;
    private String productUuid;
    private String number;
    @Nullable
    private BarCodeType barCodeType;
}
