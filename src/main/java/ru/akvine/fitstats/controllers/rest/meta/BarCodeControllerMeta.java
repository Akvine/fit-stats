package ru.akvine.fitstats.controllers.rest.meta;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.akvine.fitstats.controllers.rest.dto.barcode.AddBarCodeRequest;
import ru.akvine.fitstats.controllers.rest.dto.barcode.GetBarCodeByNumberRequest;
import ru.akvine.fitstats.controllers.rest.dto.barcode.ListBarCodeRequest;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;

import javax.validation.Valid;

@RequestMapping(value = "/barcodes")
public interface BarCodeControllerMeta {
    @PostMapping(value = "/add/number")
    Response add(@Valid @RequestBody AddBarCodeRequest request);

    @PostMapping(value = "/add/photo")
    Response add(@RequestParam(value = "productUuid") String productUuid,
                 @RequestParam(value = "photo") MultipartFile photo);

    @GetMapping(value = "/list")
    Response list(@Valid @RequestBody ListBarCodeRequest request);

    @GetMapping(value = "/get/number")
    Response get(@Valid @RequestBody GetBarCodeByNumberRequest request);

    @GetMapping(value = "/get/photo")
    Response get(MultipartFile photo);
}
