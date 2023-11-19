package ru.akvine.fitstats.controllers.rest.security;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.fitstats.controllers.rest.converter.security.RegistrationConverter;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.dto.common.SuccessfulResponse;
import ru.akvine.fitstats.controllers.rest.dto.security.registration.*;
import ru.akvine.fitstats.controllers.rest.meta.security.RegistrationControllerMeta;
import ru.akvine.fitstats.controllers.rest.validators.security.RegistrationValidator;
import ru.akvine.fitstats.services.dto.client.ClientBean;
import ru.akvine.fitstats.services.dto.security.registration.RegistrationActionRequest;
import ru.akvine.fitstats.services.dto.security.registration.RegistrationActionResult;
import ru.akvine.fitstats.services.security.RegistrationActionService;
import ru.akvine.fitstats.utils.SecurityUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class RegistrationController implements RegistrationControllerMeta {
    private final RegistrationValidator registrationValidator;
    private final RegistrationConverter registrationConverter;
    private final RegistrationActionService registrationActionService;

    @Override
    public Response start(@Valid RegistrationStartRequest request, HttpServletRequest httpServletRequest) {
        registrationValidator.verifyRegistrationLogin(request);
        RegistrationActionRequest registrationActionRequest = registrationConverter.convertToRegistrationActionRequest(request, httpServletRequest);
        RegistrationActionResult registrationActionResult = registrationActionService.startRegistration(registrationActionRequest);
        return registrationConverter.convertToRegistrationResponse(registrationActionResult);
    }

    @Override
    public Response check(@Valid RegistrationCheckOtpRequest request, HttpServletRequest httpServletRequest) {
        registrationValidator.verifyRegistrationLogin(request);
        RegistrationActionRequest registrationActionRequest = registrationConverter.convertToRegistrationActionRequest(request, httpServletRequest);
        RegistrationActionResult registrationActionResult = registrationActionService.checkOneTimePassword(registrationActionRequest);
        return registrationConverter.convertToRegistrationResponse(registrationActionResult);
    }

    @Override
    public Response newotp(@Valid RegistrationNewOtpRequest request, HttpServletRequest httpServletRequest) {
        registrationValidator.verifyRegistrationLogin(request);
        RegistrationActionRequest registrationActionRequest = registrationConverter.convertToRegistrationActionRequest(request, httpServletRequest);
        RegistrationActionResult registrationActionResult = registrationActionService.generateNewOneTimePassword(registrationActionRequest);
        return registrationConverter.convertToRegistrationResponse(registrationActionResult);
    }

    @Override
    public Response passwordValidate(@Valid RegistrationPasswordValidateRequest request) {
        registrationValidator.verifyRegistrationPassword(request);
        return new SuccessfulResponse();
    }

    @Override
    public Response finish(@Valid RegistrationFinishRequest request, HttpServletRequest httpServletRequest) {
        registrationValidator.verifyRegistrationFinish(request);
        RegistrationActionRequest registrationActionRequest = registrationConverter.convertToRegistrationActionRequest(request, httpServletRequest);
        ClientBean clientBean = registrationActionService.finishRegistration(registrationActionRequest);
        SecurityUtils.authenticate(clientBean);
        return new SuccessfulResponse();
    }
}
