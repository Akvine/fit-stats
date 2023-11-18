package ru.akvine.fitstats.controllers.rest.meta;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.akvine.fitstats.controllers.rest.dto.admin.DeleteProductRequest;
import ru.akvine.fitstats.controllers.rest.dto.admin.SecretRequest;
import ru.akvine.fitstats.controllers.rest.dto.admin.UpdateProductRequest;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;

import javax.validation.Valid;

@RequestMapping(value = "/admin")
public interface AdminControllerMeta {
    @PostMapping(value = "/products/update")
    Response productUpdate(@Valid @RequestBody UpdateProductRequest request);

    @PostMapping(value = "/products/delete")
    Response productDelete(@Valid @RequestBody DeleteProductRequest request);

    @GetMapping(value = "/products/export")
    ResponseEntity productExport(@Valid @RequestBody SecretRequest request);
}
