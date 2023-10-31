package ru.akvine.fitstats.controllers.rest.converter;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.rest.dto.admin.UpdateProductRequest;
import ru.akvine.fitstats.controllers.rest.dto.product.ProductDto;
import ru.akvine.fitstats.controllers.rest.dto.product.ProductResponse;
import ru.akvine.fitstats.enums.VolumeMeasurement;
import ru.akvine.fitstats.services.dto.product.ProductBean;
import ru.akvine.fitstats.services.dto.product.UpdateProduct;
import ru.akvine.fitstats.utils.MathUtils;

@Component
public class AdminConverter {
    private static final int PRODUCT_ROUND_VALUE_ACCURACY = 1;

    public UpdateProduct convertToUpdateProduct(UpdateProductRequest request) {
        return new UpdateProduct()
                .setUuid(request.getUuid())
                .setMeasurement(StringUtils.isBlank(request.getMeasurement()) ? null : VolumeMeasurement.valueOf(request.getMeasurement()))
                .setTitle(request.getTitle())
                .setProducer(request.getProducer())
                .setProteins(request.getProteins())
                .setFats(request.getFats())
                .setCarbohydrates(request.getCarbohydrates())
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
                .setVolume(productBean.getVolume());
    }
}
