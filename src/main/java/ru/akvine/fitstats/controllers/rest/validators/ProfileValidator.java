package ru.akvine.fitstats.controllers.rest.validators;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.rest.dto.profile.UpdateBiometricRequest;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.validation.ValidationException;
import ru.akvine.fitstats.validators.ConverterTypeValidator;
import ru.akvine.fitstats.validators.DurationValidator;
import ru.akvine.fitstats.validators.PhysicalActivitiesValidator;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ProfileValidator {
    private final DurationValidator durationValidator;
    private final ConverterTypeValidator converterTypeValidator;
    private final PhysicalActivitiesValidator physicalActivitiesValidator;

    public void verifyRecordsDownload(LocalDate startDate,
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

        if (startDate != null && endDate != null && StringUtils.isBlank(duration)) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Profile.PROFILE_RECORDS_DOWNLOAD_ERROR,
                    "All params (startDate, endDate, duration) are presented! Fill starDate and endDate or duration");
        }
        durationValidator.validate(duration);
        if (converterType != null) {
            converterTypeValidator.validate(converterType);
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
}
