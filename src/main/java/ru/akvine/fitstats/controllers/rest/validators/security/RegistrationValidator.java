package ru.akvine.fitstats.controllers.rest.validators.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.rest.dto.security.LoginRequest;
import ru.akvine.fitstats.controllers.rest.dto.security.registration.RegistrationFinishRequest;
import ru.akvine.fitstats.controllers.rest.dto.security.registration.RegistrationPasswordValidateRequest;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.client.ClientAlreadyExistsException;
import ru.akvine.fitstats.exceptions.validation.ValidationException;
import ru.akvine.fitstats.services.client.ClientService;
import ru.akvine.fitstats.validators.*;

@Component
@RequiredArgsConstructor
public class RegistrationValidator {
    private final EmailValidator emailValidator;
    private final PasswordValidator passwordValidator;
    private final GenderValidator genderValidator;
    private final HeightMeasurementValidator heightMeasurementValidator;
    private final WeightMeasurementValidator weightMeasurementValidator;
    private final DietBaseValidator dietBaseValidator;
    private final PhysicalActivitiesValidator physicalActivitiesValidator;
    private final ClientService clientService;

    private static final int ZERO = 0;
    private static final int INVALID_AGE = 150;

    public void verifyRegistrationLogin(LoginRequest request) {
        String login = request.getLogin();
        emailValidator.validate(login);
        verifyNotExistsByLogin(request.getLogin());
    }

    public void verifyRegistrationPassword(RegistrationPasswordValidateRequest request) {
        passwordValidator.validate(request.getPassword());
    }

    public void verifyRegistrationFinish(RegistrationFinishRequest request) {
        try {
            if (request.getAge() < ZERO) {
                throw new ValidationException(
                        CommonErrorCodes.Validation.Biometric.AGE_INVALID_ERROR, "Age can't be less than 0. Field name: age");
            }
            if (request.getAge() > INVALID_AGE) {
                throw new ValidationException(
                        CommonErrorCodes.Validation.Biometric.AGE_INVALID_ERROR, "Age can't be more than 150. Field name: age");
            }
            if (Float.parseFloat(request.getWeight()) < ZERO) {
                throw new ValidationException(
                        CommonErrorCodes.Validation.Biometric.WEIGHT_INVALID_ERROR, "Weight can't be less than 0. Field name: weight");
            }
            if (Float.parseFloat(request.getHeight()) < ZERO) {
                throw new ValidationException(
                        CommonErrorCodes.Validation.Biometric.HEIGHT_INVALID_ERROR, "Height can't be less than 0. Field name: height");
            }
        } catch (NumberFormatException exception) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Biometric.FIELD_NUMBER_INVALID,
                    "Next fields: [weight, height] can be only number!"
            );
        }

        passwordValidator.validate(request.getPassword());
        emailValidator.validate(request.getLogin());
        genderValidator.validate(request.getGender());
        heightMeasurementValidator.validate(request.getHeightMeasurement());
        weightMeasurementValidator.validate(request.getWeightMeasurement());
        dietBaseValidator.validate(request.getDiet());
        physicalActivitiesValidator.validate(request.getPhysicalActivity());
        verifyNotExistsByLogin(request.getLogin());
    }

    public void verifyNotExistsByLogin(String login) {
        boolean exists = clientService.isExistsByEmail(login);
        if (exists) {
            throw new ClientAlreadyExistsException("Client with email = [" + login + "] already exists!");
        }
    }
}
