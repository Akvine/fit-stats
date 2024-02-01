package ru.akvine.fitstats.controllers.rest.converter;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.rest.converter.parser.macronutrient.MacronutrientParser;
import ru.akvine.fitstats.controllers.rest.dto.product.*;
import ru.akvine.fitstats.enums.VolumeMeasurement;
import ru.akvine.fitstats.services.dto.product.Filter;
import ru.akvine.fitstats.services.dto.product.ProductBean;
import ru.akvine.fitstats.services.properties.PropertyParseService;
import ru.akvine.fitstats.utils.MathUtils;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductConverter {
    private static final int PRODUCT_ROUND_VALUE_ACCURACY = 1;
    private static final int DEFAULT_PRODUCT_VOLUME = 100;
    private final MacronutrientParser macronutrientParser;
    private final PropertyParseService propertyParseService;

    @Value("round.accuracy")
    private String roundAccuracy;

    public ProductBean convertToProductBean(AddProductRequest request) {
        Preconditions.checkNotNull(request, "addProductRequest is null");
        return new ProductBean()
                .setProteins(request.getProteins())
                .setFats(request.getFats())
                .setCarbohydrates(request.getCarbohydrates())
                .setVol(request.getVol())
                .setTitle(request.getTitle())
                .setProducer(request.getProducer())
                .setVolume(DEFAULT_PRODUCT_VOLUME)
                .setMeasurement(VolumeMeasurement.safeValueOf(request.getVolumeMeasurement()));
    }

    public ProductResponse convertToProductResponse(ProductBean productBean) {
        Preconditions.checkNotNull(productBean, "productBean is null");
        return new ProductResponse().setProduct(buildProductDto(productBean));
    }

    public ProductListResponse convertToProductListResponse(List<ProductBean> products) {
        Preconditions.checkNotNull(products, "products is null");
        return new ProductListResponse()
                .setProducts(products.stream().map(this::buildProductDto).collect(Collectors.toList()));
    }

    public Filter convertToFilter(ListProductRequest request) {
        Preconditions.checkNotNull(request, "listProductRequest is null");

        Filter filter = new Filter();
        if (StringUtils.isNotBlank(request.getMacronutrientsFilter())) {
            filter.setMacronutrientFilterParts(macronutrientParser.parse(request.getMacronutrientsFilter()));
        }

        return filter
                .setFilterName(request.getFilter());
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
                .setVol(MathUtils.round(productBean.getVol(), propertyParseService.parseInteger(roundAccuracy)))
                .setMeasurement(productBean.getMeasurement().toString())
                .setVolume(productBean.getVolume());
    }
}
