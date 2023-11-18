package ru.akvine.fitstats.validators;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.enums.Gender;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.validation.ValidationException;

@Component
public class GenderValidator implements Validator<String> {
    @Override
    public void validate(String gender) {
        if (StringUtils.isBlank(gender)) {
            throw new ValidationException(CommonErrorCodes.Validation.Biometric.GENDER_BLANK_ERROR,
                    "Gender is blank. Field name: gender");
        }
        try {
            Gender.valueOf(gender);
        } catch (IllegalArgumentException exception) {
            throw new ValidationException(CommonErrorCodes.Validation.Biometric.GENDER_INVALID_ERROR,
                    "Gender is invalid. Field name: gender");
        }
    }
}
