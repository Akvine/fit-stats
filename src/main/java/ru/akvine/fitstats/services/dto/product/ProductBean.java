package ru.akvine.fitstats.services.dto.product;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.entities.ProductEntity;
import ru.akvine.fitstats.enums.VolumeMeasurement;
import ru.akvine.fitstats.services.dto.base.SoftBean;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class ProductBean extends SoftBean {
    private Long id;
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

    public ProductBean(ProductEntity productEntity) {
        this.id = productEntity.getId();
        this.uuid = productEntity.getUuid();
        this.title = productEntity.getTitle();
        this.producer = productEntity.getProducer();
        this.proteins = productEntity.getProteins();
        this.fats = productEntity.getFats();
        this.carbohydrates = productEntity.getCarbohydrates();
        this.calories = productEntity.getCalories();
        this.volume = productEntity.getVolume();
        this.vol = productEntity.getVol();
        this.measurement = productEntity.getMeasurement();
    }
}
