package ru.akvine.fitstats.services;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.controllers.rest.dto.admin.ImportProducts;
import ru.akvine.fitstats.controllers.rest.dto.admin.file.ProductCsvRow;
import ru.akvine.fitstats.enums.ConverterType;
import ru.akvine.fitstats.enums.VolumeMeasurement;
import ru.akvine.fitstats.services.dto.admin.ProductExport;
import ru.akvine.fitstats.services.dto.product.ProductBean;
import ru.akvine.fitstats.services.dto.product.UpdateProduct;
import ru.akvine.fitstats.services.processors.format.Converter;

import java.util.Arrays;
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

    private final static String COMMA = ",";

    @Autowired
    public AdminService(ProductService productService,
                        List<Converter> converters) {
        this.productService = productService;
        this.availableConverters = converters
                .stream()
                .collect(toMap(Converter::getType, identity()));
    }

    public byte[] exportProducts(ConverterType converterType) {
        Preconditions.checkNotNull(converterType, "converterType is null");
        List<ProductExport> productsExport = productService
                .getAll()
                .stream()
                .map(ProductExport::new)
                .collect(Collectors.toList());
        return availableConverters
                .get(converterType)
                .convert(productsExport, ProductExport.class);
    }

    public void importProducts(ImportProducts importProducts) {
        Preconditions.checkNotNull(importProducts, "importProducts is null");

        List<?> records = importProducts.getRecords();
        records.forEach(record -> {
            if (record instanceof ProductCsvRow) {
                ProductCsvRow csvRow = (ProductCsvRow) record;
                ProductBean productBean = new ProductBean()
                        .setUuid(csvRow.getUuid())
                        .setProteins(Double.parseDouble(csvRow.getProteins()))
                        .setFats(Double.parseDouble(csvRow.getFats()))
                        .setCarbohydrates(Double.parseDouble(csvRow.getCarbohydrates()))
                        .setVol(Double.parseDouble(csvRow.getVol()))
                        .setVolume(Double.parseDouble(csvRow.getVolume()))
                        .setTitle(csvRow.getTitle())
                        .setProducer(csvRow.getProducer())
                        .setMeasurement(VolumeMeasurement.safeValueOf(csvRow.getMeasurement()))
                        .setCategoriesTitles(Arrays.stream(csvRow.getCategoriesTitles().split(COMMA)).collect(Collectors.toSet()));
                productService.add(productBean);
            }
        });
    }

    public ProductBean updateProduct(UpdateProduct updateProduct) {
        Preconditions.checkNotNull(updateProduct, "updateProduct is null");
        return productService.update(updateProduct);
    }

    public void deleteProduct(String productUuid) {
        Preconditions.checkNotNull(productUuid, "product uuid is null");
        productService.deleteByUuid(productUuid);
    }
}
