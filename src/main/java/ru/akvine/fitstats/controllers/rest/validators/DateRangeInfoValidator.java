package ru.akvine.fitstats.controllers.rest.validators;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.rest.dto.statistic.DateRangeInfo;
import ru.akvine.fitstats.controllers.rest.dto.statistic.DateRangeRequest;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.validation.ValidationException;
import ru.akvine.fitstats.validators.DurationValidator;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DateRangeInfoValidator {
    private final DurationValidator durationValidator;

    public void verifyDateRangeRequest(DateRangeRequest dateRangeRequest) {
        DateRangeInfo dateRangeInfo = dateRangeRequest.getDateRangeInfo();
        LocalDate startDate = dateRangeInfo.getStartDate();
        LocalDate endDate = dateRangeInfo.getEndDate();
        String duration = dateRangeInfo.getDuration();

        if (StringUtils.isBlank(duration) && startDate == null && endDate == null) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Statistic.DATE_RANGE_VALUES_EMPTY,
                    "Date range fields startDate, endDate and duration is empty. Need to fill duration or startDate with endDate");
        }

        if (startDate != null
                && endDate != null
                && startDate.isAfter(endDate)) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Statistic.START_DATE_AFTER_END_DATE_ERROR,
                    "Start date can't be after end date!");
        }

        if (startDate != null && endDate != null && StringUtils.isNotBlank(duration)) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Statistic.ILLEGAL_DATE_RANGE_STATE_ERROR,
                    "Start date, end date and duration is fill! Need to fill duration or startDate with endDate");
        }
        if (StringUtils.isNotBlank(duration)) {
            durationValidator.validate(duration);
        }
    }
}
