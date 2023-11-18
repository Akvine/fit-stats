package ru.akvine.fitstats.services;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.services.dto.product.ProductBean;
import ru.akvine.fitstats.services.dto.product.UpdateProduct;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {
    private final ProductService productService;

    public ProductBean updateProduct(UpdateProduct updateProduct) {
        Preconditions.checkNotNull(updateProduct, "updateProduct is null");
        return productService.update(updateProduct);
    }

    public void deleteProduct(String productUuid) {
        Preconditions.checkNotNull(productUuid, "product uuid is null");
        productService.deleteByUuid(productUuid);
    }
}
