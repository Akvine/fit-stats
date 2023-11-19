package ru.akvine.fitstats.controllers.rest.security;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.fitstats.controllers.rest.converter.security.AuthConverter;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.dto.common.SuccessfulResponse;
import ru.akvine.fitstats.controllers.rest.dto.security.auth.AuthCredentialsRequest;
import ru.akvine.fitstats.controllers.rest.dto.security.auth.AuthFinishRequest;
import ru.akvine.fitstats.controllers.rest.dto.security.auth.AuthNewOtpRequest;
import ru.akvine.fitstats.controllers.rest.meta.security.AuthControllerMeta;
import ru.akvine.fitstats.controllers.rest.validators.security.AuthValidator;
import ru.akvine.fitstats.services.dto.client.ClientBean;
import ru.akvine.fitstats.services.dto.security.auth.AuthActionRequest;
import ru.akvine.fitstats.services.dto.security.auth.AuthActionResult;
import ru.akvine.fitstats.services.security.AuthActionService;
import ru.akvine.fitstats.utils.SecurityUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthControllerMeta {
    private final AuthValidator authValidator;
    private final AuthConverter authConverter;
    private final AuthActionService authActionService;

    @Override
    public Response start(@Valid AuthCredentialsRequest request, HttpServletRequest httpServletRequest) {
        authValidator.verifyAuthLogin(request);
        AuthActionRequest authActionRequest = authConverter.convertToAuthActionRequest(request, httpServletRequest);
        AuthActionResult authActionResult = authActionService.startAuth(authActionRequest);
        return authConverter.convertToAuthResponse(authActionResult);
    }

    @Override
    public Response newotp(@Valid AuthNewOtpRequest request, HttpServletRequest httpServletRequest) {
        authValidator.verifyAuthLogin(request);
        AuthActionResult authActionResult = authActionService.generateNewOtp(request.getLogin());
        return authConverter.convertToAuthResponse(authActionResult);
    }

    @Override
    public Response finish(@Valid AuthFinishRequest request, HttpServletRequest httpServletRequest) {
        authValidator.verifyAuthLogin(request);
        AuthActionRequest authActionRequest = authConverter.convertToAuthActionRequest(request, httpServletRequest);
        ClientBean clientBean = authActionService.finishAuth(authActionRequest);
        SecurityUtils.authenticate(clientBean);
        return new SuccessfulResponse();
    }

    @Override
    public Response logout(HttpServletRequest httpServletRequest) {
        SecurityUtils.doLogout(httpServletRequest);
        return new SuccessfulResponse();
    }
}
