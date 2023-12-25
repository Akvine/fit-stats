package ru.akvine.fitstats.validators;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.enums.Duration;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.validation.ValidationException;

@Component
@Slf4j
public class DurationValidator implements Validator<String> {
    @Override
    public void validate(String duration) {
        if (StringUtils.isBlank(duration)) {
            logger.info("Duration value is blank");
            throw new ValidationException(CommonErrorCodes.Validation.Statistic.DURATION_BLANK_ERROR,
                    "Duration is blank. Field name: duration");
        }
        try {
            Duration.valueOf(duration);
        } catch (IllegalArgumentException exception) {
            logger.info("Duration value is invalid = {}", duration);
            throw new ValidationException(CommonErrorCodes.Validation.Statistic.DURATION_INVALID_ERROR,
                    "Duration is invalid. Field name: duration");
        }
    }
}
