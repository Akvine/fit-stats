package ru.akvine.fitstats.controllers.rest.meta.profile;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.dto.profile.delete.ProfileDeleteFinishRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

public interface ProfileDeleteControllerMeta extends ProfileControllerMeta {

    @PostMapping(value = "/delete/start")
    Response deleteStart(HttpServletRequest httpServletRequest);

    @PostMapping(value = "/delete/newotp")
    Response deleteNewOtp();

    @PostMapping(value = "/delete/finish")
    Response deleteFinish(@Valid @RequestBody ProfileDeleteFinishRequest request, HttpServletRequest httpServletRequest);
}
