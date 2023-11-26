package ru.akvine.fitstats.controllers.rest.validators;

import com.google.common.base.Preconditions;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.rest.dto.weight.ChangeWeightRequest;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.validation.ValidationException;

@Component
public class WeightValidator {
    public void verifyChangeWeightRequest(ChangeWeightRequest request) {
        Preconditions.checkNotNull(request, "changeWeightRequest is null");

        double value;
        try {
            value = Double.parseDouble(request.getWeight());
        } catch (NumberFormatException exception) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Weight.WEIGHT_CHANGE_ERROR,
                    "Weight must be a number! Field : weight"
            );
        }

        if (value < 0) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Weight.WEIGHT_INVALID_ERROR,
                    "Weight can't be less than 0. Field : weight"
            );
        }
    }
}
