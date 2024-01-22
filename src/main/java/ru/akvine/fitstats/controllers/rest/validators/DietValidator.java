package ru.akvine.fitstats.controllers.rest.validators;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.rest.dto.diet.ChangeDietRequest;
import ru.akvine.fitstats.controllers.rest.dto.diet.DeleteRecordRequest;
import ru.akvine.fitstats.controllers.rest.dto.statistic.DateRangeInfo;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.validation.ValidationException;
import ru.akvine.fitstats.validators.DietBaseValidator;

@Component
@RequiredArgsConstructor
public class DietValidator {
    private final DietBaseValidator dietBaseValidator;
    private final DateRangeInfoValidator dateRangeInfoValidator;

    public void verifyUpdateDietRequest(ChangeDietRequest request) {
        dietBaseValidator.validate(request.getDietType());
    }

    public void verifyDeleteDietRequest(DeleteRecordRequest request) {
        String uuid = request.getUuid();
        DateRangeInfo dateRangeInfo = request.getDateRangeInfo();

        if (dateRangeInfo != null && StringUtils.isNotBlank(uuid)) {
            throw new ValidationException(CommonErrorCodes.Validation.Diet.ALL_PARAMETERS_PRESENTED_ERROR,
                    "All parameters are presented: [uuid] and date range. Only one need");
        }

        if (dateRangeInfo == null && StringUtils.isBlank(uuid)) {
            throw new ValidationException(CommonErrorCodes.Validation.Diet.PARAMETERS_NOT_PRESENTED_ERROR,
                    "Parameters are presented: [uuid] or [start] with [end] in dateRangeInfo dto.");
        }

        if (dateRangeInfo != null) {
            dateRangeInfoValidator.verifyDateRangeRequest(request);
        }
    }
}
