package ru.akvine.fitstats.controllers.rest.meta.security;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.dto.security.registration.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequestMapping(value = "/registration")
public interface RegistrationControllerMeta {
    @PostMapping(value = "/start")
    Response start(@Valid @RequestBody RegistrationStartRequest request, HttpServletRequest httpServletRequest);

    @PostMapping(value = "/check")
    Response check(@Valid @RequestBody RegistrationCheckOtpRequest request, HttpServletRequest httpServletRequest);

    @PostMapping(value = "/newotp")
    Response newotp(@Valid @RequestBody RegistrationNewOtpRequest request, HttpServletRequest httpServletRequest);

    @PostMapping(value = "/password/validate")
    Response passwordValidate(@Valid @RequestBody RegistrationPasswordValidateRequest request);

    @PostMapping(value = "/finish")
    Response finish(@Valid @RequestBody RegistrationFinishRequest request, HttpServletRequest httpServletRequest);
}
