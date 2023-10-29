package ru.akvine.fitstats.validators;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.enums.VolumeMeasurement;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.validation.ValidationException;

@Component
public class VolumeMeasurementValidator implements Validator<String> {
    @Override
    public void validate(String measurement) {
        if (StringUtils.isBlank(measurement)) {
            throw new ValidationException(CommonErrorCodes.Validation.Product.VOLUME_MEASUREMENT_BLANK_ERROR,
                    "Volume measurement is blank. Field name: measurement");
        }
        try {
            VolumeMeasurement.valueOf(measurement);
        } catch (IllegalArgumentException exception) {
            throw new ValidationException(CommonErrorCodes.Validation.Product.VOLUME_MEASUREMENT_INVALID_ERROR,
                    "Volume measurement is invalid. Field name: measurement");
        }
    }
}
