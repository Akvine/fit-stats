package ru.akvine.fitstats.controllers.rest.converter;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.akvine.fitstats.controllers.rest.converter.parser.Parser;
import ru.akvine.fitstats.controllers.rest.dto.admin.*;
import ru.akvine.fitstats.controllers.rest.dto.admin.file.ProductCsvRow;
import ru.akvine.fitstats.controllers.rest.dto.product.ProductDto;
import ru.akvine.fitstats.controllers.rest.dto.product.ProductResponse;
import ru.akvine.fitstats.enums.ConverterType;
import ru.akvine.fitstats.enums.VolumeMeasurement;
import ru.akvine.fitstats.services.dto.product.ProductBean;
import ru.akvine.fitstats.services.dto.product.UpdateProduct;
import ru.akvine.fitstats.utils.MathUtils;
import ru.akvine.fitstats.utils.SecurityUtils;

import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Component
public class AdminConverter {
    private static final int PRODUCT_ROUND_VALUE_ACCURACY = 1;
    private static final String HEADER_PREFIX = "attachment; filename=";
    private static final String DEFAULT_FILE_NAME = "file";
    private static final String POINT = ".";

    private final Map<ConverterType, Parser> availableParsers;

    @Autowired
    public AdminConverter(List<Parser> parsers) {
        this.availableParsers =
                parsers
                        .stream()
                        .collect(toMap(Parser::getType, identity()));
    }

    public ConverterType convertToConverterType(ExportProductsRequest request) {
        String converterType = request.getConverterType();
        if (StringUtils.isBlank(converterType)) {
            return ConverterType.CSV;
        }
        return ConverterType.valueOf(converterType);
    }

    public ResponseEntity convertToExportResponse(String filename, byte[] file, ConverterType converterType) {
        Preconditions.checkNotNull(file, "products is null");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, resolveHeaderType(filename, converterType))
                .contentType(MediaType.parseMediaType(resolveMediaType(converterType)))
                .body(file);
    }

    public ImportProducts convertToImportProduct(String converterType, MultipartFile file) {
        Preconditions.checkNotNull(converterType, "convertType is null");
        Preconditions.checkNotNull(file, "file is null");

        ConverterType type = ConverterType.valueOf(converterType);
        return new ImportProducts()
                .setClientUuid(SecurityUtils.getCurrentUser().getUuid())
                .setRecords(availableParsers
                        .get(type)
                        .parse(file, resolveClass(type)));
    }

    public InvalidProductsRowsResponse convertToInvalidProductsRowsResponse(List<InvalidProductRow> invalidProductRows) {
        Preconditions.checkNotNull(invalidProductRows, "invalidProductRows is null");
        return new InvalidProductsRowsResponse()
                .setInvalidRows(invalidProductRows);
    }

    public UpdateProduct convertToUpdateProduct(UpdateProductRequest request) {
        return new UpdateProduct()
                .setClientUuid(SecurityUtils.getCurrentUser().getUuid())
                .setUuid(request.getUuid())
                .setMeasurement(StringUtils.isBlank(request.getMeasurement()) ? null : VolumeMeasurement.safeValueOf(request.getMeasurement()))
                .setTitle(request.getTitle())
                .setProducer(request.getProducer())
                .setProteins(request.getProteins())
                .setFats(request.getFats())
                .setCarbohydrates(request.getCarbohydrates())
                .setVol(request.getVol())
                .setVolume(request.getVolume());
    }

    public ProductResponse convertToProductResponse(ProductBean productBean) {
        Preconditions.checkNotNull(productBean, "productBean is null");
        return new ProductResponse().setProduct(buildProductDto(productBean));
    }

    private ProductDto buildProductDto(ProductBean productBean) {
        return new ProductDto()
                .setTitle(productBean.getTitle())
                .setProducer(productBean.getProducer())
                .setUuid(productBean.getUuid())
                .setProteins(MathUtils.round(productBean.getProteins(), PRODUCT_ROUND_VALUE_ACCURACY))
                .setFats(MathUtils.round(productBean.getFats(), PRODUCT_ROUND_VALUE_ACCURACY))
                .setCalories(MathUtils.round(productBean.getCalories(), PRODUCT_ROUND_VALUE_ACCURACY))
                .setCarbohydrates(MathUtils.round(productBean.getCarbohydrates(), PRODUCT_ROUND_VALUE_ACCURACY))
                .setMeasurement(productBean.getMeasurement().toString())
                .setVol(productBean.getVol())
                .setVolume(productBean.getVolume());
    }

    private String resolveHeaderType(String filename, ConverterType converterType) {
        StringBuilder builder = new StringBuilder();
        builder.append(HEADER_PREFIX);

        if (StringUtils.isBlank(filename)) {
            builder
                    .append(DEFAULT_FILE_NAME);
        } else {
            builder
                    .append(filename);
        }
        builder.append(POINT);
        builder.append(converterType.getValue());
        return builder.toString();
    }

    private String resolveMediaType(ConverterType converterType) {
        switch (converterType) {
            case CSV:
                return "application/csv";
            default:
                throw new IllegalArgumentException("Converter with type = [" + converterType + "] is not supported!");
        }
    }

    private Class resolveClass(ConverterType converterType) {
        switch (converterType) {
            case CSV:
                return ProductCsvRow.class;
            default:
                throw new IllegalArgumentException("Converter with type = [" + converterType + "] is not supported!");
        }
    }
}
