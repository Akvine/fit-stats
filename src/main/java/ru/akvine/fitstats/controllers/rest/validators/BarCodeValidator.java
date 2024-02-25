package ru.akvine.fitstats.controllers.rest.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.rest.dto.barcode.AddBarCodeRequest;
import ru.akvine.fitstats.validators.BarCodeTypeValidator;

@Component
@RequiredArgsConstructor
public class BarCodeValidator {
    private final BarCodeTypeValidator barCodeTypeValidator;

    public void verifyAddBarCodeRequest(AddBarCodeRequest request) {
        if (request.getType() != null) {
            barCodeTypeValidator.validate(request.getType());
        }
    }
}
