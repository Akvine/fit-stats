package ru.akvine.fitstats.controllers.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.fitstats.controllers.rest.converter.ProductConverter;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.dto.product.AddProductRequest;
import ru.akvine.fitstats.controllers.rest.dto.product.ListProductRequest;
import ru.akvine.fitstats.controllers.rest.meta.ProductControllerMeta;
import ru.akvine.fitstats.controllers.rest.validators.ProductValidator;
import ru.akvine.fitstats.services.ProductService;
import ru.akvine.fitstats.services.dto.product.Filter;
import ru.akvine.fitstats.services.dto.product.ProductBean;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ProductController implements ProductControllerMeta {
    private final ProductService productService;
    private final ProductValidator productValidator;
    private final ProductConverter productConverter;

    @Override
    public Response add(@Valid AddProductRequest request) {
        productValidator.verifyAddProductRequest(request);
        ProductBean productBean = productConverter.convertToProductBean(request);
        ProductBean savedProductBean = productService.add(productBean);
        return productConverter.convertToProductResponse(savedProductBean);
    }

    @Override
    public Response list(@Valid ListProductRequest request) {
        productValidator.verifyListProductRequest(request);
        Filter filter = productConverter.convertToFilter(request);
        List<ProductBean> products = productService.findByFilter(filter);
        return productConverter.convertToProductListResponse(products);
    }
}
