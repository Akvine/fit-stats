package ru.akvine.fitstats.controllers.rest.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.fitstats.controllers.rest.admin.meta.AdminBarCodeControllerMeta;
import ru.akvine.fitstats.controllers.rest.converter.AdminConverter;
import ru.akvine.fitstats.controllers.rest.dto.admin.barcode.DeleteBarCodeRequest;
import ru.akvine.fitstats.controllers.rest.dto.admin.barcode.UpdateBarCodeRequest;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.dto.common.SuccessfulResponse;
import ru.akvine.fitstats.controllers.rest.validators.AdminValidator;
import ru.akvine.fitstats.services.AdminService;
import ru.akvine.fitstats.services.dto.barcode.BarCodeBean;
import ru.akvine.fitstats.services.dto.barcode.DeleteBarCode;
import ru.akvine.fitstats.services.dto.barcode.UpdateBarCode;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AdminBarCodeController implements AdminBarCodeControllerMeta {
    private final AdminValidator adminValidator;
    private final AdminService adminService;
    private final AdminConverter adminConverter;

    @Override
    public Response update(@Valid @RequestBody UpdateBarCodeRequest request) {
        adminValidator.verifyUpdateBarCodeRequest(request);
        UpdateBarCode updateBarCode = adminConverter.convertToUpdateBarCode(request);
        BarCodeBean updatedBarCode = adminService.updateBarCode(updateBarCode);
        return adminConverter.convertToUpdateBarCodeResponse(updatedBarCode);
    }

    @Override
    public Response delete(@Valid @RequestBody DeleteBarCodeRequest request) {
        adminValidator.verifySecret(request.getSecret());
        DeleteBarCode deleteBarCode = adminConverter.convertToDeleteBarCode(request);
        adminService.deleteBarCode(deleteBarCode);
        return new SuccessfulResponse();
    }
}
