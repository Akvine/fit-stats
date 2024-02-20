package ru.akvine.fitstats.validators;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.enums.ConverterType;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.validation.ValidationException;

@Component
@Slf4j
public class ConverterTypeValidator implements Validator<String> {
    @Override
    public void validate(String converterType) {
        if (StringUtils.isBlank(converterType)) {
            throw new ValidationException(CommonErrorCodes.Validation.Profile.PROFILE_RECORDS_DOWNLOAD_ERROR,
                    "Converter type is blank. Field name: converterType");
        }
        try {
            ConverterType.valueOf(converterType.toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new ValidationException(CommonErrorCodes.Validation.Profile.PROFILE_RECORDS_DOWNLOAD_ERROR,
                    "Converter type is invalid. Field name: converterType");
        }
    }
}
