package ru.akvine.fitstats.controllers.rest.meta.profile;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.dto.profile.change_email.ProfileChangeEmailFinishRequest;
import ru.akvine.fitstats.controllers.rest.dto.profile.change_email.ProfileChangeEmailStartRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

public interface ProfileChangeEmailControllerMeta extends ProfileControllerMeta {
    @PostMapping(value = "/change/email/start")
    Response changeEmailStart(@Valid @RequestBody ProfileChangeEmailStartRequest request, HttpServletRequest httpServletRequest);

    @PostMapping(value = "/change/email/newotp")
    Response changeEmailNewOtp();

    @PostMapping(value = "/change/email/finish")
    Response changeEmailFinish(@Valid @RequestBody ProfileChangeEmailFinishRequest request, HttpServletRequest httpServletRequest);
}
