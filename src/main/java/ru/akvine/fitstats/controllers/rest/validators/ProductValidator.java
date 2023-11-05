package ru.akvine.fitstats.controllers.rest.validators;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.rest.dto.product.AddProductRequest;
import ru.akvine.fitstats.controllers.rest.dto.product.ListProductRequest;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.validation.ValidationException;
import ru.akvine.fitstats.validators.VolumeMeasurementValidator;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductValidator {
    @Value("${processors.supported.macronutrients}")
    private List<String> supportedMacronutrients;

    private final VolumeMeasurementValidator volumeMeasurementValidator;

    public void verifyAddProductRequest(AddProductRequest request) {
        Preconditions.checkNotNull(request, "addProductRequest is null");

        if (StringUtils.isBlank(request.getTitle())) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Product.TITLE_BLANK_ERROR,
                    "Title is blank. Field name: title"
            );
        }

        if (StringUtils.isBlank(request.getProducer())) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Product.PRODUCER_BLANK_ERROR,
                    "Producer is blank. Field name: producer"
            );
        }

        if (request.getProteins() < 0) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Product.MACRONUTRIENT_VALUE_INVALID_ERROR,
                    "Proteins is less than 0. Field name: proteins");
        }
        if (request.getFats() < 0) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Product.MACRONUTRIENT_VALUE_INVALID_ERROR,
                    "Fats is less than 0. Field name: fats");
        }
        if (request.getCarbohydrates() < 0) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Product.MACRONUTRIENT_VALUE_INVALID_ERROR,
                    "Carbohydrates is less than 0. Field name: carbohydrates");
        }
        if (request.getVolume() < 0) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Product.VOLUME_VALUE_INVALID_ERROR,
                    "Volume is less than 0. Field name: volume"
            );
        }
        volumeMeasurementValidator.validate(request.getVolumeMeasurement());
    }

    public void verifyListProductRequest(ListProductRequest request) {
        Preconditions.checkNotNull(request, "listProductRequest is null");
        if (request.getMacronutrients() != null) {
            request.getMacronutrients().forEach((key, value) -> {
                if (!supportedMacronutrients.contains(key)) {
                    String errorMessage = String.format("Macronutrient with name = [%s] not supported!", key);
                    throw new ValidationException(CommonErrorCodes.Validation.Product.MACRONUTRIENT_VALUE_INVALID_ERROR,
                            errorMessage);
                }
            });
        }
    }
}
