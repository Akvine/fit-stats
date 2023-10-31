package ru.akvine.fitstats.services.dto.admin;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.services.dto.product.ProductBean;

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
        this.proteins = productBean.getProteins();
        this.fats = productBean.getFats();
        this.carbohydrates = productBean.getCarbohydrates();
        this.calories = productBean.getCalories();
        this.volume = productBean.getVolume();
        this.measurement = productBean.getMeasurement().toString();
    }
}
