package ru.akvine.fitstats.controllers.rest.converter;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.context.ClientSettingsContext;
import ru.akvine.fitstats.controllers.rest.converter.parser.macronutrient.MacronutrientParser;
import ru.akvine.fitstats.controllers.rest.converter.scanner.barcode.BarCodeScanResult;
import ru.akvine.fitstats.controllers.rest.dto.product.*;
import ru.akvine.fitstats.enums.VolumeMeasurement;
import ru.akvine.fitstats.services.dto.product.Filter;
import ru.akvine.fitstats.services.dto.product.GetByBarCode;
import ru.akvine.fitstats.services.dto.product.ProductBean;
import ru.akvine.fitstats.utils.SecurityUtils;

import java.util.List;
import java.util.stream.Collectors;

import static ru.akvine.fitstats.utils.MathUtils.round;

@Component
@RequiredArgsConstructor
public class ProductConverter {
    private static final int DEFAULT_PRODUCT_VOLUME = 100;

    private static final double ALCOHOL_COEFFICIENT = 0.789;
    private final MacronutrientParser macronutrientParser;

    public ProductBean convertToProductBean(AddProductRequest request) {
        Preconditions.checkNotNull(request, "addProductRequest is null");
        ProductBean productBean = new ProductBean()
                .setProteins(request.getProteins())
                .setFats(request.getFats())
                .setCarbohydrates(request.getCarbohydrates())
                .setTitle(request.getTitle())
                .setProducer(request.getProducer())
                .setVolume(DEFAULT_PRODUCT_VOLUME)
                .setClientUuid(SecurityUtils.getCurrentUser().getUuid())
                .setMeasurement(VolumeMeasurement.safeValueOf(request.getVolumeMeasurement()));


        Double vol = request.getVol();
        Double alcohol = request.getAlcohol();
        if (vol == null && alcohol == null) {
            return productBean
                    .setAlcohol(0)
                    .setVol(0);
        } else if (vol != null && alcohol == null) {
            return productBean
                    .setVol(vol)
                    .setAlcohol(vol * ALCOHOL_COEFFICIENT);
        } else {
            return productBean
                    .setVol(alcohol / ALCOHOL_COEFFICIENT)
                    .setAlcohol(alcohol);
        }
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

    public GetByBarCode convertToGetByBarCode(GetByBarcodeNumberRequest request) {
        Preconditions.checkNotNull(request, "getByBarcodeNumberRequest is null");
        return new GetByBarCode()
                .setClientUuid(SecurityUtils.getCurrentUser().getUuid())
                .setNumber(request.getNumber());
    }

    public GetByBarCode convertToGetByBarCode(BarCodeScanResult result) {
        Preconditions.checkNotNull(result, "barCodeScanResult is null");
        return new GetByBarCode()
                .setClientUuid(SecurityUtils.getCurrentUser().getUuid())
                .setNumber(result.getNumber());
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
        int roundAccuracy = ClientSettingsContext.getClientSettingsContextHolder().getBySessionForCurrent().getRoundAccuracy();
        return new ProductDto()
                .setTitle(productBean.getTitle())
                .setProducer(productBean.getProducer())
                .setUuid(productBean.getUuid())
                .setProteins(round(productBean.getProteins(), roundAccuracy))
                .setFats(round(productBean.getFats(), roundAccuracy))
                .setCalories(round(productBean.getCalories(), roundAccuracy))
                .setAlcohol(round(productBean.getAlcohol(), roundAccuracy))
                .setCarbohydrates(round(productBean.getCarbohydrates(), roundAccuracy))
                .setVol(round(productBean.getVol(), roundAccuracy))
                .setMeasurement(productBean.getMeasurement().toString())
                .setVolume(round(productBean.getVolume(), roundAccuracy));
    }
}
