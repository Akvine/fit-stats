package ru.akvine.fitstats.controllers.rest.validators;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.akvine.fitstats.controllers.rest.dto.admin.*;
import ru.akvine.fitstats.controllers.rest.dto.admin.file.ProductCsvRow;
import ru.akvine.fitstats.controllers.rest.dto.admin.file.ProductXlsxRow;
import ru.akvine.fitstats.controllers.rest.validators.wrapper.AdminValidatorCsvWrapper;
import ru.akvine.fitstats.controllers.rest.validators.wrapper.AdminValidatorXlsxWrapper;
import ru.akvine.fitstats.enums.FileType;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.validation.ValidationException;
import ru.akvine.fitstats.services.properties.PropertyParseService;
import ru.akvine.fitstats.validators.ConverterTypeValidator;
import ru.akvine.fitstats.validators.VolumeMeasurementValidator;
import ru.akvine.fitstats.validators.file.FileValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Component
public class AdminValidator {
    @Value("admin.secret")
    private String secret;
    @Value("file.converter.max-rows.limit")
    private String maxRowsLimit;

    private final VolumeMeasurementValidator volumeMeasurementValidator;
    private final ConverterTypeValidator converterTypeValidator;
    private final Map<FileType, FileValidator> availableFileValidators;
    private final AdminValidatorCsvWrapper adminValidatorCsvWrapper;
    private final AdminValidatorXlsxWrapper adminValidatorXlsxWrapper;
    private final PropertyParseService propertyParseService;

    public AdminValidator(VolumeMeasurementValidator volumeMeasurementValidator,
                          ConverterTypeValidator converterTypeValidator,
                          List<FileValidator> fileValidators,
                          AdminValidatorCsvWrapper adminValidatorCsvWrapper,
                          AdminValidatorXlsxWrapper adminValidatorXlsxWrapper,
                          PropertyParseService propertyParseService) {
        this.volumeMeasurementValidator = volumeMeasurementValidator;
        this.adminValidatorCsvWrapper = adminValidatorCsvWrapper;
        this.adminValidatorXlsxWrapper = adminValidatorXlsxWrapper;
        this.converterTypeValidator = converterTypeValidator;
        this.propertyParseService = propertyParseService;
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

    public List<InvalidProductRow> verifyImportProducts(ImportProducts importProducts) {
        int rowsCount = importProducts.getRecords().size();
        if (rowsCount > propertyParseService.parseInteger(maxRowsLimit)) {
            String message = String.format("File rows count = [%s] greater than limit [%s]!", rowsCount, maxRowsLimit);
            throw new ValidationException(
                    CommonErrorCodes.Validation.FILE_MAX_ROWS_COUNT_INVALID_ERROR,
                    message
            );
        }

        List<?> records = importProducts.getRecords();
        List<InvalidProductRow> invalidProductRows = new ArrayList<>();
        records.forEach(record -> {
            if (record instanceof ProductCsvRow) {
                InvalidProductRow invalidProductRow = adminValidatorCsvWrapper.wrap((ProductCsvRow) record);
                if (invalidProductRow != null) {
                    invalidProductRows.add(invalidProductRow);
                }
            } else if (record instanceof ProductXlsxRow) {
                InvalidProductRow invalidProductRow = adminValidatorXlsxWrapper.wrap((ProductXlsxRow) record);
                if (invalidProductRow != null) {
                    invalidProductRows.add(invalidProductRow);
                }
            } else {
                throw new IllegalStateException("Invalid records format");
            }
        });
        return invalidProductRows;
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

        if (updateProductRequest.getVol() != null && (updateProductRequest.getVol() < 0 || updateProductRequest.getVol() > 100)) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Admin.MACRONUTRIENT_INVALID_ERROR,
                    "VOL is less than 0 or greater than 100. Field name: volume");
        }

        if (StringUtils.isNotBlank(updateProductRequest.getMeasurement())) {
            volumeMeasurementValidator.validate(updateProductRequest.getMeasurement());
        }
    }

    public void verifyDeleteProductRequest(DeleteProductRequest request) {
        Preconditions.checkNotNull(request, "deleteProductRequest is null");
        verifySecret(request.getSecret());
    }

    public void verifyBlockClientRequest(BlockClientRequest request) {
        Preconditions.checkNotNull(request, "blockClientRequest is null");
        verifySecret(request.getSecret());

        if (StringUtils.isBlank(request.getEmail()) && StringUtils.isBlank(request.getUuid())) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.FIELD_NOT_PRESENTED_ERROR,
                    "Uuid or email not presented. Must present uuid or email");
        }
    }

    public void verifyUnblockClientRequest(UnblockClientRequest request) {
        Preconditions.checkNotNull(request, "unblockClientRequest is null");
        verifySecret(request.getSecret());

        if (StringUtils.isBlank(request.getEmail()) && StringUtils.isBlank(request.getUuid())) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.FIELD_NOT_PRESENTED_ERROR,
                    "Uuid or email not presented. Must present uuid or email");
        }
    }

    public void verifySecret(String secret) {
        String adminSecret = propertyParseService.get(this.secret);
        if (!adminSecret.equals(secret)) {
            throw new BadCredentialsException("Bad credentials!");
        }
    }
}
