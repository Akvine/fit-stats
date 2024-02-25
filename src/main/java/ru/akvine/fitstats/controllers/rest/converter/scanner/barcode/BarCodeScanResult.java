package ru.akvine.fitstats.controllers.rest.converter.scanner.barcode;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.enums.BarCodeType;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class BarCodeScanResult {
    private String number;
    private BarCodeType barCodeType;
}
