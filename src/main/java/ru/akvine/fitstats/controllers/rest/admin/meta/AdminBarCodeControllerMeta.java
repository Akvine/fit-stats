package ru.akvine.fitstats.controllers.rest.admin.meta;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.akvine.fitstats.controllers.rest.dto.admin.barcode.DeleteBarCodeRequest;
import ru.akvine.fitstats.controllers.rest.dto.admin.barcode.UpdateBarCodeRequest;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;

import javax.validation.Valid;

@RequestMapping(value = "/admin/barcode")
public interface AdminBarCodeControllerMeta {
    @PostMapping(value = "/update")
    Response update(@Valid @RequestBody UpdateBarCodeRequest request);

    @PostMapping(value = "/delete")
    Response delete(@Valid @RequestBody DeleteBarCodeRequest request);
}
