package ru.akvine.fitstats.validators.telegram;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.enums.telegram.PrintMacronutrientsMode;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.validation.ValidationException;
import ru.akvine.fitstats.validators.Validator;

@Component
public class PrintMacronutrientsModeValidator implements Validator<String> {
    @Override
    public void validate(String mode) {
        if (StringUtils.isBlank(mode)) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Profile.PROFILE_SETTINGS_PRINT_MODE_BLANK_ERROR,
                    "Telegram print macronutrients mode is blank. Field name: telegramPrintMacronutrientsMode");
        }

        try {
            PrintMacronutrientsMode.valueOf(mode.toUpperCase());
        } catch (Exception exception) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Profile.PROFILE_SETTINGS_PRINT_MODE_INVALID_ERROR,
                    "Telegram print macronutrients mode is blank. Field name: telegramPrintMacronutrientsMode");
        }
    }
}
