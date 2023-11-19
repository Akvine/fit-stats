package ru.akvine.fitstats.controllers.rest.validators;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.rest.dto.client.ClientLoginRequest;
import ru.akvine.fitstats.controllers.rest.dto.client.ClientRegisterRequest;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.validation.ValidationException;
import ru.akvine.fitstats.validators.*;

@Component
@RequiredArgsConstructor
public class ClientValidator {
    private final EmailValidator emailValidator;
    private final GenderValidator genderValidator;
    private final WeightMeasurementValidator weightMeasurementValidator;
    private final HeightMeasurementValidator heightMeasurementValidator;
    private final PhysicalActivitiesValidator physicalActivitiesValidator;
    private final DietBaseValidator dietBaseValidator;

    public void verifyClientRegisterRequest(ClientRegisterRequest request) {
        Preconditions.checkNotNull(request, "clientRegisterRequest is null");


        emailValidator.validate(request.getEmail());
        genderValidator.validate(request.getGender());
        heightMeasurementValidator.validate(request.getHeightMeasurement());
        weightMeasurementValidator.validate(request.getWeightMeasurement());
        physicalActivitiesValidator.validate(request.getPhysicalActivity());
        dietBaseValidator.validate(request.getDiet());
    }

    public void verifyClientLoginRequest(ClientLoginRequest request) {
        Preconditions.checkNotNull(request, "clientLoginRequest is null");
        emailValidator.validate(request.getEmail());
    }
}
