package ru.akvine.fitstats.controllers.rest.validators;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.validation.ValidationException;
import ru.akvine.fitstats.validators.ConverterTypeValidator;
import ru.akvine.fitstats.validators.DurationValidator;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ProfileValidator {
    private final DurationValidator durationValidator;
    private final ConverterTypeValidator converterTypeValidator;

    public void verifyRecordsDownload(LocalDate startDate,
                                      LocalDate endDate,
                                      String duration,
                                      String converterType) {
        if (startDate == null && endDate == null && StringUtils.isBlank(duration)) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Profile.PROFILE_RECORDS_DOWNLOAD_ERROR,
                    "All params (startDate, endDate, duration) is empty!");
        }

        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Profile.PROFILE_RECORDS_DOWNLOAD_ERROR,
                    "Start date can't be after end date!");
        }

        if (startDate != null && endDate != null && StringUtils.isBlank(duration)) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Profile.PROFILE_RECORDS_DOWNLOAD_ERROR,
                    "All params (startDate, endDate, duration) are presented! Fill starDate and endDate or duration");
        }
        durationValidator.validate(duration);
        if (converterType != null) {
            converterTypeValidator.validate(converterType);
        }
    }
}
