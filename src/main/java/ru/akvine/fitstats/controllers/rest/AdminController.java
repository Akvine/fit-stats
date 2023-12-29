package ru.akvine.fitstats.controllers.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.akvine.fitstats.controllers.rest.converter.AdminConverter;
import ru.akvine.fitstats.controllers.rest.dto.admin.*;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.dto.common.SuccessfulResponse;
import ru.akvine.fitstats.controllers.rest.meta.AdminControllerMeta;
import ru.akvine.fitstats.controllers.rest.validators.AdminValidator;
import ru.akvine.fitstats.enums.ConverterType;
import ru.akvine.fitstats.services.AdminService;
import ru.akvine.fitstats.services.dto.product.ProductBean;
import ru.akvine.fitstats.services.dto.product.UpdateProduct;
import ru.akvine.fitstats.utils.SecurityUtils;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AdminController implements AdminControllerMeta {
    private final AdminValidator adminValidator;
    private final AdminConverter adminConverter;
    private final AdminService adminService;

    // TODO: Надо ограничить права с помощью ролей, чтобы мог только саппорт использовать

    @Override
    public ResponseEntity productsExport(@Valid ExportProductsRequest exportProductsRequest) {
        adminValidator.verifyExportProductsRequest(exportProductsRequest);
        ConverterType converterType = adminConverter.convertToConverterType(exportProductsRequest);
        byte[] file = adminService.exportProducts(converterType);
        return adminConverter.convertToExportResponse(exportProductsRequest.getFilename(), file, converterType);
    }

    @Override
    public Response productsImport(String secret, String converterType, MultipartFile file) {
        adminValidator.verifyImportProducts(secret, converterType, file);
        ImportProducts importProducts = adminConverter.convertToImportProduct(converterType, file);
        List<InvalidProductRow> invalidProductRows = adminValidator.verifyImportProducts(importProducts);
        if (!invalidProductRows.isEmpty()) {
            return adminConverter.convertToInvalidProductsRowsResponse(invalidProductRows);
        }
        adminService.importProducts(importProducts);
        return new SuccessfulResponse();
    }

    @Override
    public Response productUpdate(@Valid UpdateProductRequest request) {
        adminValidator.verifyUpdateProductRequest(request);
        UpdateProduct updateProduct = adminConverter.convertToUpdateProduct(request);
        ProductBean updatedProduct = adminService.updateProduct(updateProduct);
        return adminConverter.convertToProductResponse(updatedProduct);
    }

    @Override
    public Response productDelete(@Valid DeleteProductRequest request) {
        adminValidator.verifyDeleteProductRequest(request);
        adminService.deleteProduct(request.getUuid());
        return new SuccessfulResponse();
    }
}
