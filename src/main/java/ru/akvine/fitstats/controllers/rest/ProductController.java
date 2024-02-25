package ru.akvine.fitstats.controllers.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.akvine.fitstats.controllers.rest.converter.ProductConverter;
import ru.akvine.fitstats.controllers.rest.converter.scanner.barcode.BarCodeScanResult;
import ru.akvine.fitstats.controllers.rest.converter.scanner.barcode.BarCodeScanner;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.dto.product.AddProductRequest;
import ru.akvine.fitstats.controllers.rest.dto.product.GetByBarcodeNumberRequest;
import ru.akvine.fitstats.controllers.rest.dto.product.ListProductRequest;
import ru.akvine.fitstats.controllers.rest.meta.ProductControllerMeta;
import ru.akvine.fitstats.controllers.rest.validators.ProductValidator;
import ru.akvine.fitstats.services.ProductService;
import ru.akvine.fitstats.services.dto.product.Filter;
import ru.akvine.fitstats.services.dto.product.GetByBarCode;
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
    private final BarCodeScanner barCodeScanner;

    @Override
    public Response add(@Valid AddProductRequest request) {
        productValidator.verifyAddProductRequest(request);
        ProductBean addProductStart = productConverter.convertToProductBean(request);
        ProductBean addProductFinish = productService.add(addProductStart);
        return productConverter.convertToProductResponse(addProductFinish);
    }

    @Override
    public Response list(@Valid ListProductRequest request) {
        Filter filter = productConverter.convertToFilter(request);
        List<ProductBean> products = productService.findByFilter(filter);
        return productConverter.convertToProductListResponse(products);
    }

    @Override
    public Response get(@Valid GetByBarcodeNumberRequest request) {
        GetByBarCode getByBarCode = productConverter.convertToGetByBarCode(request);
        ProductBean productBean = productService.getByBarCodeNumber(getByBarCode);
        return productConverter.convertToProductResponse(productBean);
    }

    @Override
    public Response get(MultipartFile barcode) {
        BarCodeScanResult result = barCodeScanner.scan(barcode);
        GetByBarCode getByBarCode = productConverter.convertToGetByBarCode(result);
        ProductBean productBean = productService.getByBarCodeNumber(getByBarCode);
        return productConverter.convertToProductResponse(productBean);
    }
}
