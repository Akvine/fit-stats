package ru.akvine.fitstats.controllers.rest.validators;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.akvine.fitstats.controllers.rest.dto.admin.*;
import ru.akvine.fitstats.enums.FileType;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.validation.ValidationException;
import ru.akvine.fitstats.validators.ConverterTypeValidator;
import ru.akvine.fitstats.validators.VolumeMeasurementValidator;
import ru.akvine.fitstats.validators.file.FileValidator;

import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Component
public class AdminValidator {
    @Value("${admin.secret}")
    private String secret;
    @Value("${file.converter.max-rows.limit}")
    private int maxRowsLimit;

    private final VolumeMeasurementValidator volumeMeasurementValidator;
    private final ConverterTypeValidator converterTypeValidator;
    private final Map<FileType, FileValidator> availableFileValidators;

    public AdminValidator(VolumeMeasurementValidator volumeMeasurementValidator,
                          ConverterTypeValidator converterTypeValidator,
                          List<FileValidator> fileValidators) {
        this.volumeMeasurementValidator = volumeMeasurementValidator;
        this.converterTypeValidator = converterTypeValidator;
        this.availableFileValidators = fileValidators
                .stream()
                .collect(toMap(FileValidator::getType, identity()));
    }

    public void verifyExportProductsRequest(ExportProductsRequest exportProductsRequest) {
        Preconditions.checkNotNull(exportProductsRequest, "exportProductsRequest is null");

        verifySecret(exportProductsRequest.getSecret());
        if (StringUtils.isNotBlank(exportProductsRequest.getConverterType())) {
            converterTypeValidator.validate(exportProductsRequest.getConverterType());
        }
    }

    public void verifyImportProducts(String secret, String converterType, MultipartFile file) {
        verifySecret(secret);
        converterTypeValidator.validate(converterType);
        FileType type = FileType.valueOf(converterType);
        availableFileValidators
                .get(type)
                .validate(file);
    }

    public void verifyImportProducts(ImportProducts importProducts) {
        // TODO : добавить валидацию записей

        int rowsCount = importProducts.getRecords().size();
        if (rowsCount > maxRowsLimit) {
            String message = String.format("File rows count = [%s] greater than limit [%s]!", rowsCount, maxRowsLimit);
            throw new ValidationException(
                    CommonErrorCodes.Validation.FILE_MAX_ROWS_COUNT_INVALID_ERROR,
                    message
            );
        }
    }

    public void verifyUpdateProductRequest(UpdateProductRequest updateProductRequest) {
        Preconditions.checkNotNull(updateProductRequest, "updateProductRequest is null");

        verifySecret(updateProductRequest.getSecret());

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

    public void verifyDeleteProductRequest(DeleteProductRequest request) {
        Preconditions.checkNotNull(request, "deleteProductRequest is null");
        verifySecret(request.getSecret());
    }

    public void verifySecret(String secret) {
        if (!this.secret.equals(secret)) {
            throw new BadCredentialsException("Bad credentials!");
        }
    }
}
