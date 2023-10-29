package ru.akvine.fitstats.validators;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.enums.WeightMeasurement;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.validation.ValidationException;

@Component
public class WeightMeasurementValidator implements Validator<String> {
    @Override
    public void validate(String measurement) {
        if (StringUtils.isBlank(measurement)) {
            throw new ValidationException(CommonErrorCodes.Validation.Biometric.WEIGHT_MEASUREMENT_BLANK_ERROR,
                    "Weight measurement is blank. Field name: weightMeasurement");
        }
        try {
            WeightMeasurement.valueOf(measurement);
        } catch (IllegalArgumentException exception) {
            throw new ValidationException(CommonErrorCodes.Validation.Biometric.WEIGHT_MEASUREMENT_INVALID_ERROR,
                    "Weight measurement is invalid. Field name: weightMeasurement");
        }
    }
}
