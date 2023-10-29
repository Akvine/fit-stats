package ru.akvine.fitstats.controllers.rest.meta;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.akvine.fitstats.controllers.rest.dto.client.ClientLoginRequest;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.dto.client.ClientRegisterRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequestMapping(value = "/clients")
public interface ClientControllerMeta {
    @PostMapping("/register")
    Response register(@Valid @RequestBody ClientRegisterRequest request);

    @PostMapping("/login")
    Response login(@Valid @RequestBody ClientLoginRequest request);

    @PostMapping("/logout")
    Response logout(HttpServletRequest request);
}
