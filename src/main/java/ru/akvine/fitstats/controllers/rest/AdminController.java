package ru.akvine.fitstats.controllers.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.fitstats.controllers.rest.converter.AdminConverter;
import ru.akvine.fitstats.controllers.rest.dto.admin.DeleteProductRequest;
import ru.akvine.fitstats.controllers.rest.dto.admin.SecretRequest;
import ru.akvine.fitstats.controllers.rest.dto.admin.UpdateProductRequest;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.dto.common.SuccessfulResponse;
import ru.akvine.fitstats.controllers.rest.meta.AdminControllerMeta;
import ru.akvine.fitstats.controllers.rest.validators.AdminValidator;
import ru.akvine.fitstats.services.AdminService;
import ru.akvine.fitstats.services.dto.product.ProductBean;
import ru.akvine.fitstats.services.dto.product.UpdateProduct;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AdminController implements AdminControllerMeta {
    private final AdminValidator adminValidator;
    private final AdminConverter adminConverter;
    private final AdminService adminService;

    // TODO: Надо ограничить права с помощью ролей, чтобы мог только саппорт использовать

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

    @Override
    public ResponseEntity productExport(@Valid SecretRequest request) {
        adminValidator.verifySecret(request);
        byte[] file = adminService.export();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=file.csv")
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }
}
