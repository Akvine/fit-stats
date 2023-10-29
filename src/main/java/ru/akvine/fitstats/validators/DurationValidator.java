package ru.akvine.fitstats.validators;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.enums.Duration;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.validation.ValidationException;

@Component
public class DurationValidator implements Validator<String> {
    @Override
    public void validate(String duration) {
        if (StringUtils.isBlank(duration)) {
            throw new ValidationException(CommonErrorCodes.Validation.Statistic.DURATION_BLANK_ERROR,
                    "Duration is blank. Field name: duration");
        }
        try {
            Duration.valueOf(duration);
        } catch (IllegalArgumentException exception) {
            throw new ValidationException(CommonErrorCodes.Validation.Statistic.DURATION_INVALID_ERROR,
                    "Duration is invalid. Field name: duration");
        }
    }
}
