package ru.akvine.fitstats.validators;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.enums.Macronutrient;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.validation.ValidationException;

@Component
public class MacronutrientValidator implements Validator<String> {
    @Override
    public void validate(String macronutrient) {
        if (StringUtils.isBlank(macronutrient)) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Statistic.MACRONUTRIENT_BLANK_ERROR,
                    "Macronutrient is blank. Field name: macronutrient"
            );
        }
        try {
            Macronutrient.valueOf(macronutrient);
        } catch (IllegalArgumentException exception) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Statistic.MACRONUTRIENT_INVALID_ERROR,
                    "Macronutrient is invalid. Field name: macronutrient"
            );
        }
    }
}
