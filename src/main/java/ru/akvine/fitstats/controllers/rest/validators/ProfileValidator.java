package ru.akvine.fitstats.controllers.rest.validators;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.akvine.fitstats.controllers.rest.dto.profile.ImportRecords;
import ru.akvine.fitstats.controllers.rest.dto.profile.UpdateBiometricRequest;
import ru.akvine.fitstats.controllers.rest.dto.profile.UpdateSettingsRequest;
import ru.akvine.fitstats.controllers.rest.dto.profile.change_email.ProfileChangeEmailStartRequest;
import ru.akvine.fitstats.controllers.rest.dto.profile.change_password.ProfileChangePasswordStartRequest;
import ru.akvine.fitstats.enums.FileType;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.client.ClientAlreadyExistsException;
import ru.akvine.fitstats.exceptions.validation.ValidationException;
import ru.akvine.fitstats.managers.FileValidatorsManager;
import ru.akvine.fitstats.services.client.ClientService;
import ru.akvine.fitstats.services.properties.PropertyParseService;
import ru.akvine.fitstats.validators.*;
import ru.akvine.fitstats.validators.telegram.PrintMacronutrientsModeValidator;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ProfileValidator {
    private final DurationValidator durationValidator;
    private final ConverterTypeValidator converterTypeValidator;
    private final PhysicalActivitiesValidator physicalActivitiesValidator;
    private final FileValidatorsManager fileValidatorsManager;
    private final ClientService clientService;
    private final PasswordValidator passwordValidator;
    private final PropertyParseService propertyParseService;
    private final PrintMacronutrientsModeValidator printMacronutrientsModeValidator;
    private final LanguageValidator languageValidator;

    @Value("file.converter.max-rows.limit")
    private String maxRowsLimit;


    public void verifyRecordsExport(LocalDate startDate,
                                    LocalDate endDate,
                                    String duration,
                                    String converterType) {
        if (startDate == null && endDate == null && StringUtils.isBlank(duration)) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Profile.PROFILE_RECORDS_DOWNLOAD_ERROR,
                    "All params (startDate, endDate, duration) is empty!");
        }

        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Profile.PROFILE_RECORDS_DOWNLOAD_ERROR,
                    "Start date can't be after end date!");
        }

        if (startDate != null && endDate != null && StringUtils.isNotBlank(duration)) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Profile.PROFILE_RECORDS_DOWNLOAD_ERROR,
                    "All params (startDate, endDate, duration) are presented! Fill starDate and endDate or duration");
        }

        if (StringUtils.isNotBlank(duration)) {
            durationValidator.validate(duration);
        }
        if (converterType != null) {
            converterTypeValidator.validate(converterType);
        }
    }

    public void verifyRecordsImport(String converterType, MultipartFile file) {
        converterTypeValidator.validate(converterType);
        FileType type = FileType.valueOf(converterType);
        fileValidatorsManager
                .getAvailableFileValidators()
                .get(type)
                .validate(file);
    }

    public void verifyRecordsCount(ImportRecords importRecords) {
        // TODO : Сделать валидацию строчек на корректность данных

        int rowsCount = importRecords.getRecords().size();
        int maxRowsLimit = propertyParseService.parseInteger(this.maxRowsLimit);
        if (rowsCount > maxRowsLimit) {
            String message = String.format("File rows count = [%s] greater than limit [%s]!", rowsCount, maxRowsLimit);
            throw new ValidationException(
                    CommonErrorCodes.Validation.FILE_MAX_ROWS_COUNT_INVALID_ERROR,
                    message
            );
        }
    }

    public void verifyUpdateBiometricRequest(UpdateBiometricRequest request) {
        Preconditions.checkNotNull(request, "updateBiometricRequest is null");

        try {
            if (request.getAge() != null && request.getAge() < 0) {
                throw new ValidationException(
                        CommonErrorCodes.Validation.Biometric.AGE_INVALID_ERROR, "Age can't be less than 0. Field name: age");
            }
            if (request.getAge() != null && request.getAge() > 150) {
                throw new ValidationException(
                        CommonErrorCodes.Validation.Biometric.AGE_INVALID_ERROR, "Age can't be more than 150. Field name: age");
            }
            if (request.getWeight() != null && Double.parseDouble(request.getWeight()) < 0) {
                throw new ValidationException(
                        CommonErrorCodes.Validation.Biometric.WEIGHT_INVALID_ERROR, "Weight can't be less than 0. Field name: weight");
            }
            if (request.getHeight() != null && Double.parseDouble(request.getHeight()) < 0) {
                throw new ValidationException(
                        CommonErrorCodes.Validation.Biometric.HEIGHT_INVALID_ERROR, "Height can't be less than 0. Field name: height");
            }
        } catch (NumberFormatException exception) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Biometric.FIELD_NUMBER_INVALID,
                    "Next fields: [weight, height] can be only number!"
            );
        }

        if (StringUtils.isNotBlank(request.getPhysicalActivity())) {
            physicalActivitiesValidator.validate(request.getPhysicalActivity());
        }
    }

    public void verifyProfileChangeEmailStartRequest(ProfileChangeEmailStartRequest request) {
        verifyNotExistsByLogin(request.getNewEmail());
    }

    public void verifyProfileChangePasswordStartRequest(ProfileChangePasswordStartRequest request) {
        if (request.getNewPassword().equals(request.getCurrentPassword())) {
            throw new ValidationException(
                    CommonErrorCodes.Security.PASSWORDS_EQUAL_ERROR,
                    "Passwords are equal. Fields: currentPassword, newPassword"
            );
        }
        passwordValidator.validate(request.getNewPassword());
    }

    public void verifyUpdateSettingsRequest(UpdateSettingsRequest request) {
        if (request.getLanguage() != null) {
            languageValidator.validate(request.getLanguage());
        }
        if (request.getTelegramPrintMacronutrientsMode() != null) {
            printMacronutrientsModeValidator.validate(request.getTelegramPrintMacronutrientsMode());
        }
        if (request.getRoundAccuracy() != null) {
            if (request.getRoundAccuracy() < 0 || request.getRoundAccuracy() > 10) {
                throw new ValidationException(
                        CommonErrorCodes.Validation.Profile.PROFILE_SETTINGS_ROUND_ACCURACY_INVALID_ERROR,
                        "Round accuracy can't be less than 0 or more than 10. Field name: roundAccuracy");
            }
        }
    }

    private void verifyNotExistsByLogin(String login) {
        boolean exists = clientService.isExistsByEmail(login);
        if (exists) {
            throw new ClientAlreadyExistsException("Client with email = [" + login + "] already exists!");
        }
    }
}
