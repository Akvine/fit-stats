package ru.akvine.fitstats.controllers.rest.validators.wrapper;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.rest.dto.admin.product.InvalidProductRow;
import ru.akvine.fitstats.controllers.rest.dto.admin.product.file.ProductCsvRow;
import ru.akvine.fitstats.entities.ProductEntity;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.repositories.ProductRepository;
import ru.akvine.fitstats.validators.VolumeMeasurementValidator;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AdminValidatorCsvWrapper implements ValidatorWrapper<ProductCsvRow, InvalidProductRow> {
    private final VolumeMeasurementValidator volumeMeasurementValidator;
    private final ProductRepository productRepository;

    @Override
    @Nullable
    public InvalidProductRow wrap(ProductCsvRow productCsvRow) {
        Map<String, String> errors = new LinkedHashMap<>();
        String rowNumber = productCsvRow.getRowNumber().trim();

        if (StringUtils.isBlank(rowNumber)) {
            errors.put(rowNumber, CommonErrorCodes.Validation.Admin.ROW_NUMBER_BLANK_ERROR);
        }

        if (!StringUtils.isNumeric(rowNumber)) {
            errors.put(rowNumber, CommonErrorCodes.Validation.Admin.ROW_NUMBER_INVALID_FORMAT_ERROR);
        }

        String uuid = productCsvRow.getUuid();
        if (StringUtils.isNotBlank(uuid)) {
            Optional<ProductEntity> productEntityOptional = productRepository.findByUuid(uuid);
            if (productEntityOptional.isPresent()) {
                errors.put(uuid, CommonErrorCodes.Validation.Admin.PRODUCT_ALREADY_EXISTS_ERROR);
            }
        }

        if (StringUtils.isBlank(productCsvRow.getTitle())) {
            errors.put(String.valueOf(productCsvRow.getTitle()), CommonErrorCodes.Validation.Admin.TITLE_BLANK_ERROR);
        }

        if (StringUtils.isBlank(productCsvRow.getProducer())) {
            errors.put(String.valueOf(productCsvRow.getProducer()), CommonErrorCodes.Validation.Admin.PRODUCER_BLANK_ERROR);
        }

        double proteins, fats, carbohydrates, calories, vol, alcohol, volume;
        try {
            proteins = Double.parseDouble(productCsvRow.getProteins());
            if (proteins < 0) {
                errors.put(String.valueOf(proteins), CommonErrorCodes.Validation.Admin.MACRONUTRIENT_INVALID_ERROR);
            }
        } catch (Exception exception) {
            errors.put(String.valueOf(productCsvRow.getProteins()), CommonErrorCodes.Validation.Admin.MACRONUTRIENT_INVALID_ERROR);
        }

        try {
            fats = Double.parseDouble(productCsvRow.getFats());
            if (fats < 0) {
                errors.put(String.valueOf(productCsvRow.getFats()), CommonErrorCodes.Validation.Admin.MACRONUTRIENT_INVALID_ERROR);
            }
        } catch (Exception exception) {
            errors.put(String.valueOf(productCsvRow.getFats()), CommonErrorCodes.Validation.Admin.MACRONUTRIENT_INVALID_ERROR);
        }

        try {
            carbohydrates = Double.parseDouble(productCsvRow.getCarbohydrates());
            if (carbohydrates < 0) {
                errors.put(String.valueOf(productCsvRow.getCarbohydrates()), CommonErrorCodes.Validation.Admin.MACRONUTRIENT_INVALID_ERROR);
            }
        } catch (Exception exception) {
            errors.put(String.valueOf(productCsvRow.getCarbohydrates()), CommonErrorCodes.Validation.Admin.MACRONUTRIENT_INVALID_ERROR);
        }

        try {
            calories = Double.parseDouble(productCsvRow.getCalories());
            if (calories < 0) {
                errors.put(productCsvRow.getCalories(), CommonErrorCodes.Validation.Admin.MACRONUTRIENT_INVALID_ERROR);;
            }
        } catch (Exception exception) {
            errors.put(String.valueOf(productCsvRow.getCalories()), CommonErrorCodes.Validation.Admin.MACRONUTRIENT_INVALID_ERROR);
        }

        try {
            vol = Double.parseDouble(productCsvRow.getVol());
            if (vol < 0 || vol > 100) {
                errors.put(productCsvRow.getVol(), CommonErrorCodes.Validation.Admin.MACRONUTRIENT_INVALID_ERROR);
            }
        } catch (Exception exception) {
            errors.put(String.valueOf(productCsvRow.getVol()), CommonErrorCodes.Validation.Admin.MACRONUTRIENT_INVALID_ERROR);
        }

        try {
            alcohol = Double.parseDouble(productCsvRow.getAlcohol());
            if (alcohol < 0) {
                errors.put(productCsvRow.getAlcohol(), CommonErrorCodes.Validation.Admin.MACRONUTRIENT_INVALID_ERROR);
            }
        } catch (Exception exception) {
            errors.put(String.valueOf(productCsvRow.getAlcohol()), CommonErrorCodes.Validation.Admin.MACRONUTRIENT_INVALID_ERROR);
        }

        try {
            volume = Double.parseDouble(productCsvRow.getVolume());
            if (volume < 0) {
                errors.put(productCsvRow.getVolume(), CommonErrorCodes.Validation.Admin.MACRONUTRIENT_INVALID_ERROR);
            }
        } catch (Exception exception) {
            errors.put(String.valueOf(productCsvRow.getVolume()), CommonErrorCodes.Validation.Admin.MACRONUTRIENT_INVALID_ERROR);
        }

        try {
            volumeMeasurementValidator.validate(productCsvRow.getMeasurement());
        } catch (Exception exception) {
            errors.put(String.valueOf(productCsvRow.getMeasurement()), CommonErrorCodes.Validation.Admin.MEASUREMENT_INVALID_ERROR);
        }

        if (errors.isEmpty()) {
            return null;
        } else {
            return new InvalidProductRow(rowNumber, errors);
        }
    }
}
