package ru.akvine.fitstats.validators;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.enums.Diet;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.validation.ValidationException;

@Component
public class DietBaseValidator implements Validator<String> {
    @Override
    public void validate(String diet) {
        if (StringUtils.isBlank(diet)) {
            throw new ValidationException(CommonErrorCodes.Validation.Biometric.DIET_BLANK_ERROR,
                    "Diet is blank. Field name: diet");
        }
        try {
            Diet.valueOf(diet);
        } catch (IllegalArgumentException exception) {
            throw new ValidationException(CommonErrorCodes.Validation.Biometric.DIET_INVALID_ERROR,
                    "Diet is invalid. Field name: diet");
        }
    }
}
