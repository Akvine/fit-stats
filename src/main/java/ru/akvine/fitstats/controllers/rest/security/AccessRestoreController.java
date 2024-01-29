package ru.akvine.fitstats.controllers.rest.security;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.fitstats.controllers.rest.converter.security.AccessRestoreConverter;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.dto.common.SuccessfulResponse;
import ru.akvine.fitstats.controllers.rest.dto.security.access_restore.AccessRestoreCheckOtpRequest;
import ru.akvine.fitstats.controllers.rest.dto.security.access_restore.AccessRestoreFinishRequest;
import ru.akvine.fitstats.controllers.rest.dto.security.access_restore.AccessRestoreStartRequest;
import ru.akvine.fitstats.controllers.rest.meta.security.AccessRestoreControllerMeta;
import ru.akvine.fitstats.controllers.rest.validators.security.AccessRestoreValidator;
import ru.akvine.fitstats.services.dto.client.ClientBean;
import ru.akvine.fitstats.services.dto.security.access_restore.AccessRestoreActionRequest;
import ru.akvine.fitstats.services.dto.security.access_restore.AccessRestoreActionResult;
import ru.akvine.fitstats.services.security.AccessRestoreActionService;
import ru.akvine.fitstats.utils.SecurityUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AccessRestoreController implements AccessRestoreControllerMeta {
    private final AccessRestoreActionService accessRestoreActionService;
    private final AccessRestoreValidator accessRestoreValidator;
    private final AccessRestoreConverter accessRestoreConverter;

    @Override
    public Response start(@Valid AccessRestoreStartRequest request, HttpServletRequest httpServletRequest) {
        accessRestoreValidator.verifyAccessRestore(request);
        AccessRestoreActionRequest actionRequest = accessRestoreConverter.convertToAccessRestoreActionRequest(request, httpServletRequest);
        AccessRestoreActionResult result = accessRestoreActionService.startAccessRestore(actionRequest);
        return accessRestoreConverter.convertToAccessRestoreResponse(result);
    }

    @Override
    public Response newotp(@Valid AccessRestoreStartRequest request, HttpServletRequest httpServletRequest) {
        accessRestoreValidator.verifyAccessRestore(request);
        AccessRestoreActionRequest actionRequest = accessRestoreConverter.convertToAccessRestoreActionRequest(request, httpServletRequest);
        AccessRestoreActionResult result = accessRestoreActionService.generateNewOneTimePassword(actionRequest);
        return accessRestoreConverter.convertToAccessRestoreResponse(result);
    }

    @Override
    public Response check(@Valid AccessRestoreCheckOtpRequest request, HttpServletRequest httpServletRequest) {
        accessRestoreValidator.verifyAccessRestore(request);
        AccessRestoreActionRequest actionRequest = accessRestoreConverter.convertToAccessRestoreActionRequest(request, httpServletRequest);
        AccessRestoreActionResult result = accessRestoreActionService.checkOneTimePassword(actionRequest);
        return accessRestoreConverter.convertToAccessRestoreResponse(result);
    }

    @Override
    public Response finish(@Valid AccessRestoreFinishRequest request, HttpServletRequest httpServletRequest) {
        accessRestoreValidator.verifyAccessRestoreFinish(request);
        AccessRestoreActionRequest actionRequest = accessRestoreConverter.convertToAccessRestoreActionRequest(request, httpServletRequest);
        ClientBean clientBean = accessRestoreActionService.finishAccessRestore(actionRequest);
        SecurityUtils.authenticate(clientBean);
        return new SuccessfulResponse();
    }
}
