package ru.akvine.fitstats.controllers.rest.meta.profile;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.dto.profile.change_password.ProfileChangePasswordFinishRequest;
import ru.akvine.fitstats.controllers.rest.dto.profile.change_password.ProfileChangePasswordStartRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

public interface ProfileChangePasswordControllerMeta extends ProfileControllerMeta {
    @PostMapping(value = "/change/password/start")
    Response changePasswordStart(@Valid @RequestBody ProfileChangePasswordStartRequest request, HttpServletRequest httpServletRequest);

    @PostMapping(value = "/change/password/newotp")
    Response changePasswordNewOtp();

    @PostMapping(value = "/change/password/finish")
    Response changePasswordFinish(@Valid @RequestBody ProfileChangePasswordFinishRequest request, HttpServletRequest httpServletRequest);
}
