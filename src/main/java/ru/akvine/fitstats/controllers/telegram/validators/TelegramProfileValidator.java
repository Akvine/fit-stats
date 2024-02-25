package ru.akvine.fitstats.controllers.telegram.validators;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.telegram.dto.profile.TelegramProfileUpdateBiometricRequest;
import ru.akvine.fitstats.controllers.telegram.dto.profile.TelegramProfileUpdateSettingsRequest;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.validation.ValidationException;
import ru.akvine.fitstats.validators.LanguageValidator;
import ru.akvine.fitstats.validators.PhysicalActivitiesValidator;
import ru.akvine.fitstats.validators.telegram.PrintMacronutrientsModeValidator;

@Component
@RequiredArgsConstructor
public class TelegramProfileValidator {
    private final PhysicalActivitiesValidator physicalActivitiesValidator;
    private final LanguageValidator languageValidator;
    private final PrintMacronutrientsModeValidator printMacronutrientsModeValidator;

    public void verifyTelegramProfileUpdateBiometricRequest(TelegramProfileUpdateBiometricRequest request) {
        Preconditions.checkNotNull(request, "telegramProfileUpdateBiometricRequest is null");

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

    public void verifyTelegramProfileUpdateSettingsRequest(TelegramProfileUpdateSettingsRequest request) {
        Preconditions.checkNotNull(request, "telegramProfileUpdateSettingsRequest is null");

        int roundAccuracy;

        try {
            roundAccuracy = Integer.parseInt(request.getRoundAccuracy());
        } catch (NumberFormatException exception) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Profile.PROFILE_SETTINGS_ROUND_ACCURACY_INVALID_ERROR,
                    "Round accuracy must be value!");
        }

        if (roundAccuracy < 0 || roundAccuracy > 10) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Profile.PROFILE_SETTINGS_ROUND_ACCURACY_INVALID_ERROR,
                    "Round accuracy can't be less than 0 or more than 10!");
        }

        languageValidator.validate(request.getLanguage());
        printMacronutrientsModeValidator.validate(request.getPrintMode());
    }
}
