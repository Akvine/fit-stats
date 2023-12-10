package ru.akvine.fitstats.controllers.telegram.validators;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.telegram.dto.product.TelegramProductAddRequest;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.validation.ValidationException;
import ru.akvine.fitstats.validators.VolumeMeasurementValidator;

@Component
@RequiredArgsConstructor
public class TelegramProductValidator {
    private final VolumeMeasurementValidator volumeMeasurementValidator;

    public void verifyTelegramProductAddRequest(TelegramProductAddRequest request) {
        Preconditions.checkNotNull(request, "telegramProductAddRequest is null");

        if (StringUtils.isBlank(request.getTitle())) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Product.TITLE_BLANK_ERROR,
                    "Title is blank"
            );
        }

        if (StringUtils.isBlank(request.getProducer())) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Product.PRODUCER_BLANK_ERROR,
                    "Producer is blank"
            );
        }

        if (request.getProteins() < 0) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Product.MACRONUTRIENT_VALUE_INVALID_ERROR,
                    "Proteins is less than 0");
        }
        if (request.getFats() < 0) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Product.MACRONUTRIENT_VALUE_INVALID_ERROR,
                    "Fats is less than 0");
        }
        if (request.getCarbohydrates() < 0) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Product.MACRONUTRIENT_VALUE_INVALID_ERROR,
                    "Carbohydrates is less than 0");
        }
        if (request.getVol() > 100) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Product.MACRONUTRIENT_VALUE_INVALID_ERROR,
                    "VOL is greater than 100");
        }
        if (request.getVol() < 0) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Product.MACRONUTRIENT_VALUE_INVALID_ERROR,
                    "VOL is less than 0");
        }

        volumeMeasurementValidator.validate(request.getVolumeMeasurement());
    }
}
