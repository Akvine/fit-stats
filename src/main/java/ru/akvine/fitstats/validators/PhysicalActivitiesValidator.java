package ru.akvine.fitstats.validators;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.enums.PhysicalActivity;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.validation.ValidationException;

@Component
public class PhysicalActivitiesValidator implements Validator<String> {
    @Override
    public void validate(String physicalActivity) {
        if (StringUtils.isBlank(physicalActivity)) {
            throw new ValidationException(CommonErrorCodes.Validation.Biometric.PHYSICAL_ACTIVITY_BLANK_ERROR,
                    "Physical activity is blank. Field name: physicalActivity");
        }
        try {
            PhysicalActivity.valueOf(physicalActivity);
        } catch (IllegalArgumentException exception) {
            throw new ValidationException(CommonErrorCodes.Validation.Biometric.PHYSICAL_ACTIVITY_INVALID_ERROR,
                    "Physical activity is invalid. Field name: physicalActivity");
        }
    }
}
