package ru.akvine.fitstats.services.dto.admin;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.services.dto.product.ProductBean;
import ru.akvine.fitstats.utils.MathUtils;

@Getter
@Setter
@Accessors(chain = true)
public class ProductRecordExport {
    private String uuid;
    private String title;
    private String producer;
    private double proteins;
    private double fats;
    private double carbohydrates;
    private double calories;
    private double volume;
    private String measurement;

    public ProductRecordExport(ProductBean productBean) {
        this.uuid = productBean.getUuid();
        this.title = productBean.getTitle();
        this.producer = productBean.getProducer();
        this.proteins = MathUtils.round(productBean.getProteins(), 2);
        this.fats = MathUtils.round(productBean.getFats(), 2);
        this.carbohydrates = MathUtils.round(productBean.getCarbohydrates(), 2);
        this.calories = MathUtils.round(productBean.getCalories(), 2);
        this.volume = productBean.getVolume();
        this.measurement = productBean.getMeasurement().toString();
    }
}
