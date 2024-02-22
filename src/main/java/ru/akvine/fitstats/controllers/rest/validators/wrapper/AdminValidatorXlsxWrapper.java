package ru.akvine.fitstats.controllers.rest.validators.wrapper;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.rest.dto.admin.InvalidProductRow;
import ru.akvine.fitstats.controllers.rest.dto.admin.file.ProductXlsxRow;
import ru.akvine.fitstats.entities.ProductEntity;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.repositories.ProductRepository;
import ru.akvine.fitstats.validators.VolumeMeasurementValidator;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AdminValidatorXlsxWrapper implements ValidatorWrapper<ProductXlsxRow, InvalidProductRow> {
    private final ProductRepository productRepository;
    private final VolumeMeasurementValidator volumeMeasurementValidator;

    @Override
    @Nullable
    public InvalidProductRow wrap(ProductXlsxRow productXlsxRow) {
        Map<String, String> errors = new LinkedHashMap<>();
        String rowNumber = productXlsxRow.getRowNumber().trim();

        if (StringUtils.isBlank(rowNumber)) {
            errors.put(rowNumber, CommonErrorCodes.Validation.Admin.ROW_NUMBER_BLANK_ERROR);
        }

        if (!StringUtils.isNumeric(rowNumber)) {
            errors.put(rowNumber, CommonErrorCodes.Validation.Admin.ROW_NUMBER_INVALID_FORMAT_ERROR);
        }

        String uuid = productXlsxRow.getUuid();
        if (StringUtils.isNotBlank(uuid)) {
            Optional<ProductEntity> productEntityOptional = productRepository.findByUuid(uuid);
            if (productEntityOptional.isPresent()) {
                errors.put(uuid, CommonErrorCodes.Validation.Admin.PRODUCT_ALREADY_EXISTS_ERROR);
            }
        }

        if (StringUtils.isBlank(productXlsxRow.getTitle())) {
            errors.put(String.valueOf(productXlsxRow.getTitle()), CommonErrorCodes.Validation.Admin.TITLE_BLANK_ERROR);
        }

        if (StringUtils.isBlank(productXlsxRow.getProducer())) {
            errors.put(String.valueOf(productXlsxRow.getProducer()), CommonErrorCodes.Validation.Admin.PRODUCER_BLANK_ERROR);
        }

        double proteins, fats, carbohydrates, calories, alcohol, vol, volume;
        try {
            proteins = Double.parseDouble(productXlsxRow.getProteins());
            if (proteins < 0) {
                errors.put(String.valueOf(proteins), CommonErrorCodes.Validation.Admin.MACRONUTRIENT_INVALID_ERROR);
            }
        } catch (Exception exception) {
            errors.put(String.valueOf(productXlsxRow.getProteins()), CommonErrorCodes.Validation.Admin.MACRONUTRIENT_INVALID_ERROR);
        }

        try {
            fats = Double.parseDouble(productXlsxRow.getFats());
            if (fats < 0) {
                errors.put(String.valueOf(productXlsxRow.getFats()), CommonErrorCodes.Validation.Admin.MACRONUTRIENT_INVALID_ERROR);
            }
        } catch (Exception exception) {
            errors.put(String.valueOf(productXlsxRow.getFats()), CommonErrorCodes.Validation.Admin.MACRONUTRIENT_INVALID_ERROR);
        }

        try {
            carbohydrates = Double.parseDouble(productXlsxRow.getCarbohydrates());
            if (carbohydrates < 0) {
                errors.put(String.valueOf(productXlsxRow.getCarbohydrates()), CommonErrorCodes.Validation.Admin.MACRONUTRIENT_INVALID_ERROR);
            }
        } catch (Exception exception) {
            errors.put(String.valueOf(productXlsxRow.getCarbohydrates()), CommonErrorCodes.Validation.Admin.MACRONUTRIENT_INVALID_ERROR);
        }

        try {
            calories = Double.parseDouble(productXlsxRow.getCalories());
            if (calories < 0) {
                errors.put(productXlsxRow.getCalories(), CommonErrorCodes.Validation.Admin.MACRONUTRIENT_INVALID_ERROR);;
            }
        } catch (Exception exception) {
            errors.put(String.valueOf(productXlsxRow.getCalories()), CommonErrorCodes.Validation.Admin.MACRONUTRIENT_INVALID_ERROR);
        }

        try {
            alcohol = Double.parseDouble(productXlsxRow.getAlcohol());
            if (alcohol < 0) {
                errors.put(productXlsxRow.getAlcohol(), CommonErrorCodes.Validation.Admin.MACRONUTRIENT_INVALID_ERROR);
            }
        } catch (Exception exception) {
            errors.put(String.valueOf(productXlsxRow.getAlcohol()), CommonErrorCodes.Validation.Admin.MACRONUTRIENT_INVALID_ERROR);
        }

        try {
            vol = Double.parseDouble(productXlsxRow.getVol());
            if (vol < 0 || vol > 100) {
                errors.put(productXlsxRow.getVol(), CommonErrorCodes.Validation.Admin.MACRONUTRIENT_INVALID_ERROR);
            }
        } catch (Exception exception) {
            errors.put(String.valueOf(productXlsxRow.getVol()), CommonErrorCodes.Validation.Admin.MACRONUTRIENT_INVALID_ERROR);
        }

        try {
            volume = Double.parseDouble(productXlsxRow.getVolume());
            if (volume < 0) {
                errors.put(productXlsxRow.getVolume(), CommonErrorCodes.Validation.Admin.MACRONUTRIENT_INVALID_ERROR);
            }
        } catch (Exception exception) {
            errors.put(String.valueOf(productXlsxRow.getVolume()), CommonErrorCodes.Validation.Admin.MACRONUTRIENT_INVALID_ERROR);
        }

        try {
            volumeMeasurementValidator.validate(productXlsxRow.getMeasurement());
        } catch (Exception exception) {
            errors.put(productXlsxRow.getMeasurement(), CommonErrorCodes.Validation.Admin.MEASUREMENT_INVALID_ERROR);
        }

        if (errors.isEmpty()) {
            return null;
        } else {
            return new InvalidProductRow(rowNumber, errors);
        }
    }
}
