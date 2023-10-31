package ru.akvine.fitstats.services;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.enums.ConverterType;
import ru.akvine.fitstats.services.dto.admin.ProductRecordExport;
import ru.akvine.fitstats.services.dto.product.ProductBean;
import ru.akvine.fitstats.services.dto.product.UpdateProduct;
import ru.akvine.fitstats.services.processors.format.Converter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Service
@Slf4j
public class AdminService {
    private final ProductService productService;
    private final Map<ConverterType, Converter> availableConverters;

    @Autowired
    public AdminService(List<Converter> converters,
                        ProductService productService) {
        this.productService = productService;
        this.availableConverters = converters
                .stream()
                .collect(toMap(Converter::getType, identity()));
    }

    public ProductBean updateProduct(UpdateProduct updateProduct) {
        Preconditions.checkNotNull(updateProduct, "updateProduct is null");
        return productService.update(updateProduct);
    }

    public void deleteProduct(String productUuid) {
        Preconditions.checkNotNull(productUuid, "product uuid is null");
        productService.deleteByUuid(productUuid);
    }

    public byte[] export() {
        List<ProductRecordExport> productsToExport = productService
                .getAll()
                .stream()
                .map(ProductRecordExport::new)
                .collect(Collectors.toList());
        return availableConverters
                .get(ConverterType.CSV)
                .convert(productsToExport, ProductRecordExport.class);
    }
}
