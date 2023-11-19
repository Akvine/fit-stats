package ru.akvine.fitstats.controllers.rest.meta.security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.dto.security.auth.AuthCredentialsRequest;
import ru.akvine.fitstats.controllers.rest.dto.security.auth.AuthFinishRequest;
import ru.akvine.fitstats.controllers.rest.dto.security.auth.AuthNewOtpRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequestMapping(value = "/auth")
public interface AuthControllerMeta {
    @PostMapping(value = "/start")
    Response start(@Valid @RequestBody AuthCredentialsRequest request, HttpServletRequest httpServletRequest);

    @PostMapping(value = "/newotp")
    Response newotp(@Valid @RequestBody AuthNewOtpRequest request, HttpServletRequest httpServletRequest);

    @PostMapping(value = "/finish")
    Response finish(@Valid @RequestBody AuthFinishRequest request, HttpServletRequest httpServletRequest);

    @GetMapping(value = "/logout")
    Response logout(HttpServletRequest httpServletRequest);
}
