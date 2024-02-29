package ru.akvine.fitstats.controllers.rest.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.fitstats.controllers.rest.converter.AdminConverter;
import ru.akvine.fitstats.controllers.rest.dto.admin.client.BlockClientRequest;
import ru.akvine.fitstats.controllers.rest.dto.admin.SecretRequest;
import ru.akvine.fitstats.controllers.rest.dto.admin.client.UnblockClientRequest;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.dto.common.SuccessfulResponse;
import ru.akvine.fitstats.controllers.rest.admin.meta.AdminClientControllerMeta;
import ru.akvine.fitstats.controllers.rest.validators.AdminValidator;
import ru.akvine.fitstats.services.AdminService;
import ru.akvine.fitstats.services.dto.admin.BlockClientEntry;
import ru.akvine.fitstats.services.dto.admin.BlockClientFinish;
import ru.akvine.fitstats.services.dto.admin.BlockClientStart;
import ru.akvine.fitstats.services.dto.admin.UnblockClient;
import ru.akvine.fitstats.utils.SecurityUtils;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AdminClientController implements AdminClientControllerMeta {
    private final AdminService adminService;
    private final AdminValidator adminValidator;
    private final AdminConverter adminConverter;

    @Override
    public Response block(@Valid BlockClientRequest request) {
        adminValidator.verifyBlockClientRequest(request);
        BlockClientStart start = adminConverter.convertToBlockClientStart(request);
        BlockClientFinish finish = adminService.blockClient(start);
        return adminConverter.convertToBlockClientResponse(finish);
    }

    @Override
    public Response list(@Valid SecretRequest request) {
        adminValidator.verifySecret(request.getSecret());
        List<BlockClientEntry> blocked = adminService.listBlocked(SecurityUtils.getCurrentUser().getName());
        return adminConverter.convertToListBlockClientResponse(blocked);
    }

    @Override
    public Response unblock(@Valid UnblockClientRequest request) {
        adminValidator.verifyUnblockClientRequest(request);
        UnblockClient unblockClient = adminConverter.convertToUnblockClient(request);
        adminService.unblockClient(unblockClient);
        return new SuccessfulResponse();
    }
}
