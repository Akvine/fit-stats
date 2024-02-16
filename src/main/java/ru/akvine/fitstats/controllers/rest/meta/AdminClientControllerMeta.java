package ru.akvine.fitstats.controllers.rest.meta;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.dto.admin.BlockClientRequest;
import ru.akvine.fitstats.controllers.rest.dto.admin.UnblockClientRequest;

import javax.validation.Valid;

@RequestMapping(value = "/admin/client")
public interface AdminClientControllerMeta {
    @PostMapping(value = "/block")
    Response block(@Valid @RequestBody BlockClientRequest request);

    @PostMapping(value = "/unblock")
    Response unblock(@Valid @RequestBody UnblockClientRequest request);

}
