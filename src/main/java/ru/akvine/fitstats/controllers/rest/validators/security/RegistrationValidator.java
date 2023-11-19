package ru.akvine.fitstats.controllers.rest.validators.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.rest.dto.security.LoginRequest;
import ru.akvine.fitstats.controllers.rest.dto.security.registration.RegistrationFinishRequest;
import ru.akvine.fitstats.controllers.rest.dto.security.registration.RegistrationPasswordValidateRequest;
import ru.akvine.fitstats.exceptions.client.ClientAlreadyExistsException;
import ru.akvine.fitstats.services.ClientService;
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

    public void verifyRegistrationLogin(LoginRequest request) {
        String login = request.getLogin();
        emailValidator.validate(login);
        verifyExistsByLogin(request.getLogin());
    }

    public void verifyRegistrationPassword(RegistrationPasswordValidateRequest request) {
        passwordValidator.validate(request.getPassword());
    }

    public void verifyRegistrationFinish(RegistrationFinishRequest request) {
        passwordValidator.validate(request.getPassword());
        emailValidator.validate(request.getLogin());
        genderValidator.validate(request.getGender());
        heightMeasurementValidator.validate(request.getHeightMeasurement());
        weightMeasurementValidator.validate(request.getWeightMeasurement());
        dietBaseValidator.validate(request.getDiet());
        physicalActivitiesValidator.validate(request.getPhysicalActivity());
        verifyExistsByLogin(request.getLogin());
    }

    public void verifyExistsByLogin(String login) {
        boolean exists = clientService.isExistsByEmail(login);
        if (exists) {
            throw new ClientAlreadyExistsException("Client with email = [" + login + "] already exists!");
        }
    }
}
