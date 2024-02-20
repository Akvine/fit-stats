package ru.akvine.fitstats.validators;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.enums.Language;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.validation.ValidationException;

@Component
public class LanguageValidator implements Validator<String> {
    @Override
    public void validate(String language) {
        if (StringUtils.isBlank(language)) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Profile.PROFILE_SETTINGS_LANGUAGE_BLANK_ERROR,
                    "Language is blank. Field name: language");
        }

        try {
            Language.valueOf(language.toUpperCase());
        } catch (Exception exception) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Profile.PROFILE_SETTINGS_LANGUAGE_INVALID_ERROR,
                    "Language is invalid. Field name: language");
        }
    }
}
