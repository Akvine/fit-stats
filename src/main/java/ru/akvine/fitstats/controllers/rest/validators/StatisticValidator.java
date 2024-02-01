package ru.akvine.fitstats.controllers.rest.validators;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.rest.dto.statistic.CalculateAdditionalStatisticRequest;
import ru.akvine.fitstats.controllers.rest.dto.statistic.CalculateDescriptiveStatisticRequest;
import ru.akvine.fitstats.controllers.rest.dto.statistic.StatisticHistoryRequest;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.validation.ValidationException;
import ru.akvine.fitstats.services.properties.PropertyParseService;
import ru.akvine.fitstats.validators.DurationValidator;
import ru.akvine.fitstats.validators.MacronutrientValidator;

@Component
@RequiredArgsConstructor
@Slf4j
public class StatisticValidator {
    @Value("processors.supported.indicators")
    private String supportedIndicators;
    @Value("processors.supported.macronutrients")
    private String supportedMacronutrients;
    @Value("product.statistic.mode.count.limit")
    private String limit;

    private final DurationValidator durationValidator;
    private final DateRangeInfoValidator dateRangeInfoValidator;
    private final MacronutrientValidator macronutrientValidator;
    private final PropertyParseService propertyParseService;

    public void verifyCalculateDescriptiveStatisticRequest(CalculateDescriptiveStatisticRequest request) {
        Preconditions.checkNotNull(request, "calculateStatisticRequest is null");

        request.getIndicators().forEach(indicator -> {
            String indicatorLower = indicator.toLowerCase();
            if (!propertyParseService.parseToList(supportedIndicators).contains(indicatorLower)) {
                throw new ValidationException(
                        CommonErrorCodes.Validation.Statistic.INDICATOR_NOT_SUPPORTED_ERROR,
                        "Indicator with type = [" + indicator + "] not supported!");
            }
        });
        request.getMacronutrients().forEach(macronutrient -> {
            String macronutrientLower = macronutrient.toLowerCase();
            if (!propertyParseService.parseToList(supportedMacronutrients).contains(macronutrientLower)) {
                throw new ValidationException(
                        CommonErrorCodes.Validation.Statistic.MACRONUTRIENT_NOT_SUPPORTED_ERROR,
                        "Macronutrient with type = [" + macronutrient + "] not supported!");
            }
        });
        verifyRoundAccuracy(request.getRoundAccuracy());
        dateRangeInfoValidator.verifyDateRangeRequest(request);
    }

    public void verifyCalculateAdditionalStatisticRequest(CalculateAdditionalStatisticRequest request) {
        Preconditions.checkNotNull(request, "calculateAdditionalStatisticRequest is null");

        if (request.getModeCount() != null) {
            if (request.getModeCount() > propertyParseService.parseInteger(limit)) {
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
        dateRangeInfoValidator.verifyDateRangeRequest(request);
    }

    public void verifyStatisticHistoryRequest(StatisticHistoryRequest request) {
        Preconditions.checkNotNull(request, "statisticHistoryRequest is null");
        durationValidator.validate(request.getDuration());
        macronutrientValidator.validate(request.getMacronutrient());
    }

    private void verifyRoundAccuracy(Integer roundAccuracy) {
        if (roundAccuracy != null && roundAccuracy < 0) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Statistic.ROUND_ACCURACY_INVALID_ERROR,
                    "Accuracy round can't be less than 0");
        }
    }
}
