package ru.akvine.fitstats.controllers.rest.meta;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.akvine.fitstats.controllers.rest.dto.admin.DeleteProductRequest;
import ru.akvine.fitstats.controllers.rest.dto.admin.ExportProductsRequest;
import ru.akvine.fitstats.controllers.rest.dto.admin.UpdateProductRequest;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;

import javax.validation.Valid;

@RequestMapping(value = "/admin/products")
public interface AdminProductControllerMeta {

    @PostMapping(value = "/export")
    ResponseEntity productsExport(@Valid @RequestBody ExportProductsRequest exportProductsRequest);

    @PostMapping(value = "/import")
    Response productsImport(
            @RequestParam(value = "secret") String secret,
            @RequestParam(value = "converterType", defaultValue = "CSV") String converterType,
            @RequestParam(value = "file") MultipartFile file);

    @PostMapping(value = "/update")
    Response productUpdate(@Valid @RequestBody UpdateProductRequest request);

    @PostMapping(value = "/delete")
    Response productDelete(@Valid @RequestBody DeleteProductRequest request);
}
