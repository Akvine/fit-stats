package ru.akvine.fitstats.controllers.rest.meta;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.dto.product.AddProductRequest;
import ru.akvine.fitstats.controllers.rest.dto.product.ListProductRequest;

import javax.validation.Valid;

@RequestMapping(value = "/products")
public interface ProductControllerMeta {
    @PostMapping
    Response add(@Valid @RequestBody AddProductRequest request);

    @GetMapping
    Response list(@Valid @RequestBody ListProductRequest request);
}
