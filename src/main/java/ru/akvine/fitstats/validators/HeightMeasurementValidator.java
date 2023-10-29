package ru.akvine.fitstats.validators;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.enums.HeightMeasurement;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.validation.ValidationException;

@Component
public class HeightMeasurementValidator implements Validator<String> {
    @Override
    public void validate(String measurement) {
        if (StringUtils.isBlank(measurement)) {
            throw new ValidationException(CommonErrorCodes.Validation.Biometric.HEIGHT_MEASUREMENT_BLANK_ERROR,
                    "Height measurement is blank. Field name: heightMeasurement");
        }
        try {
            HeightMeasurement.valueOf(measurement);
        } catch (IllegalArgumentException exception) {
            throw new ValidationException(CommonErrorCodes.Validation.Biometric.HEIGHT_MEASUREMENT_INVALID_ERROR,
                    "Height measurement is invalid. Field name: heightMeasurement");
        }
    }
}
