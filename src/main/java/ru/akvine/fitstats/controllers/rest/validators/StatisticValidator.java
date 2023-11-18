package ru.akvine.fitstats.controllers.rest.validators;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.rest.dto.statistic.CalculateAdditionalStatisticRequest;
import ru.akvine.fitstats.controllers.rest.dto.statistic.CalculateDescriptiveStatisticRequest;
import ru.akvine.fitstats.controllers.rest.dto.statistic.DateRangeInfo;
import ru.akvine.fitstats.controllers.rest.dto.statistic.DateRangeRequest;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.validation.ValidationException;
import ru.akvine.fitstats.validators.DurationValidator;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class StatisticValidator {
    @Value("${processors.supported.indicators}")
    private List<String> supportedIndicators;
    @Value("${processors.supported.macronutrients}")
    private List<String> supportedMacronutrients;
    @Value("${product.statistic.mode.count.limit}")
    private int limit;

    private final DurationValidator durationValidator;

    public void verifyCalculateDescriptiveStatisticRequest(CalculateDescriptiveStatisticRequest request) {
        Preconditions.checkNotNull(request, "calculateStatisticRequest is null");

        request.getIndicators().forEach(indicator -> {
            String indicatorLower = indicator.toLowerCase();
            if (!supportedIndicators.contains(indicatorLower)) {
                throw new ValidationException(
                        CommonErrorCodes.Validation.Statistic.INDICATOR_NOT_SUPPORTED_ERROR,
                        "Indicator with type = [" + indicator + "] not supported!");
            }
        });
        request.getMacronutrients().forEach(macronutrient -> {
            String macronutrientLower = macronutrient.toLowerCase();
            if (!supportedMacronutrients.contains(macronutrientLower)) {
                throw new ValidationException(
                        CommonErrorCodes.Validation.Statistic.MACRONUTRIENT_NOT_SUPPORTED_ERROR,
                        "Macronutrient with type = [" + macronutrient + "] not supported!");
            }
        });
        verifyRoundAccuracy(request.getRoundAccuracy());
        verifyDateRangeRequest(request);
    }

    public void verifyCalculateAdditionalStatisticRequest(CalculateAdditionalStatisticRequest request) {
        Preconditions.checkNotNull(request, "calculateAdditionalStatisticRequest is null");

        if (request.getModeCount() != null) {
            if (request.getModeCount() > limit) {
                throw new ValidationException(
                        CommonErrorCodes.Validation.Statistic.MODE_COUNT_INVALID_ERROR,
                        "Mode count can't be more than limit count. Limit count = [" + limit + "]. Field invalid: modeCount");
            }
            if (request.getModeCount() < 0) {
                throw new ValidationException(
                        CommonErrorCodes.Validation.Statistic.MODE_COUNT_INVALID_ERROR,
                        "Mode count can't be less 0. Limit count = [" + limit + "]. Field invalid: modeCount");
            }
        }
        verifyRoundAccuracy(request.getRoundAccuracy());
        verifyDateRangeRequest(request);
    }

    private void verifyRoundAccuracy(Integer roundAccuracy) {
        if (roundAccuracy != null && roundAccuracy < 0) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Statistic.ROUND_ACCURACY_INVALID_ERROR,
                    "Accuracy round can't be less than 0");
        }
    }

    private void verifyDateRangeRequest(DateRangeRequest dateRangeRequest) {
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
