package ru.akvine.fitstats.controllers.rest.meta;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.dto.product.AddProductRequest;
import ru.akvine.fitstats.controllers.rest.dto.product.GetByBarcodeNumberRequest;
import ru.akvine.fitstats.controllers.rest.dto.product.ListProductRequest;

import javax.validation.Valid;

@RequestMapping(value = "/products")
public interface ProductControllerMeta {
    @PostMapping
    Response add(@Valid @RequestBody AddProductRequest request);

    @GetMapping
    Response list(@Valid @RequestBody ListProductRequest request);

    @GetMapping(value = "/get/barcode/number")
    Response get(@Valid @RequestBody GetByBarcodeNumberRequest request);

    @GetMapping(value = "/get/barcode/photo")
    Response get(@RequestParam(value = "photo") MultipartFile barcode);
}
