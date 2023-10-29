package ru.akvine.fitstats.controllers.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.fitstats.controllers.rest.converter.ClientConverter;
import ru.akvine.fitstats.controllers.rest.dto.client.ClientLoginRequest;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.dto.client.ClientRegisterRequest;
import ru.akvine.fitstats.controllers.rest.dto.common.SuccessfulResponse;
import ru.akvine.fitstats.controllers.rest.meta.ClientControllerMeta;
import ru.akvine.fitstats.controllers.rest.validators.ClientValidator;
import ru.akvine.fitstats.services.ClientService;
import ru.akvine.fitstats.services.dto.client.ClientBean;
import ru.akvine.fitstats.services.dto.client.ClientRegister;
import ru.akvine.fitstats.utils.SecurityUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ClientController implements ClientControllerMeta {
    private final ClientService clientService;
    private final ClientConverter clientConverter;
    private final ClientValidator clientValidator;

    @Override
    public Response register(@Valid ClientRegisterRequest request) {
        clientValidator.verifyClientRegisterRequest(request);
        ClientRegister clientRegister = clientConverter.convertToClientRegister(request);
        ClientBean savedClientBean = clientService.register(clientRegister);
        SecurityUtils.authenticate(savedClientBean);
        return clientConverter.convertToClientResponse(savedClientBean);
    }

    @Override
    public Response login(@Valid ClientLoginRequest request) {
        clientValidator.verifyClientLoginRequest(request);
        ClientBean clientBean = clientConverter.convertToClientBean(request);
        clientService.login(clientBean);
        return new SuccessfulResponse();
    }

    @Override
    public Response logout(HttpServletRequest request) {
        SecurityUtils.doLogout(request);
        return new SuccessfulResponse();
    }
}
