package ru.akvine.fitstats.controllers.rest.validators;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.rest.dto.admin.UpdateProductRequest;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.validation.ValidationException;
import ru.akvine.fitstats.validators.VolumeMeasurementValidator;

@Component
@RequiredArgsConstructor
public class AdminValidator {
    private final VolumeMeasurementValidator volumeMeasurementValidator;

    public void verifyUpdateProductRequest(UpdateProductRequest updateProductRequest) {
        Preconditions.checkNotNull(updateProductRequest, "updateProductRequest is null");

        if (StringUtils.isBlank(updateProductRequest.getUuid())) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Admin.UUID_VALUE_BLANK_ERROR,
                    "Product uuid is blank. Field name: uuid");
        }

        if (updateProductRequest.getProteins() != null && updateProductRequest.getProteins() < 0) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Admin.MACRONUTRIENT_INVALID_ERROR,
                    "Proteins is less than 0. Field name: proteins");
        }

        if (updateProductRequest.getFats() != null && updateProductRequest.getFats() < 0) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Admin.MACRONUTRIENT_INVALID_ERROR,
                    "Fats is less than 0. Field name: fats");
        }

        if (updateProductRequest.getCarbohydrates() != null && updateProductRequest.getCarbohydrates() < 0) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Admin.MACRONUTRIENT_INVALID_ERROR,
                    "Carbohydrates is less than 0. Field name: carbohydrates");
        }

        if (updateProductRequest.getVolume() != null && updateProductRequest.getVolume() < 0) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Admin.MACRONUTRIENT_INVALID_ERROR,
                    "Volume is less than 0. Field name: volume");
        }

        if (StringUtils.isNotBlank(updateProductRequest.getMeasurement())) {
            volumeMeasurementValidator.validate(updateProductRequest.getMeasurement());
        }
    }
}