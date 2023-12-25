package ru.akvine.fitstats.services.dto.admin;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.enums.VolumeMeasurement;
import ru.akvine.fitstats.services.dto.product.ProductBean;

import static ru.akvine.fitstats.utils.MathUtils.round;

@Data
@Accessors(chain = true)
public class ProductExport {
    private String uuid;
    private String title;
    private String producer;
    private double proteins;
    private double fats;
    private double carbohydrates;
    private double calories;
    private double vol;
    private double volume;
    private VolumeMeasurement measurement;

    public ProductExport(ProductBean productBean) {
        this.uuid = productBean.getUuid();
        this.title = productBean.getTitle();
        this.producer = productBean.getProducer();
        this.proteins = round(productBean.getProteins(), 2);
        this.fats = round(productBean.getFats(), 2);
        this.carbohydrates = round(productBean.getCarbohydrates(), 2);
        this.calories = round(productBean.getCarbohydrates(), 2);
        this.vol = round(productBean.getVol(), 2);
        this.volume = round(productBean.getVolume(), 2);
        this.measurement = productBean.getMeasurement();
    }
}
