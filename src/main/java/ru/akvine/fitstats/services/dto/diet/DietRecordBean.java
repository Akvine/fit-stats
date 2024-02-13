package ru.akvine.fitstats.services.dto.diet;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import ru.akvine.fitstats.entities.DietRecordEntity;
import ru.akvine.fitstats.services.dto.base.Bean;
import ru.akvine.fitstats.services.dto.client.ClientBean;
import ru.akvine.fitstats.services.dto.product.ProductBean;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class DietRecordBean extends Bean {
    private Long id;
    private String uuid;
    private double volume;
    private double proteins;
    private double fats;
    private double carbohydrates;
    private double alcohol;
    private double calories;
    private ClientBean clientBean;
    private ProductBean productBean;
    private LocalDate date;
    @Nullable
    private LocalTime time;

    public DietRecordBean(DietRecordEntity dietRecordEntity) {
        this.id = dietRecordEntity.getId();
        this.uuid = dietRecordEntity.getUuid();
        this.clientBean = new ClientBean(dietRecordEntity.getClient());
        this.productBean = new ProductBean(dietRecordEntity.getProduct());
        this.proteins = dietRecordEntity.getProteins();
        this.fats = dietRecordEntity.getFats();
        this.carbohydrates = dietRecordEntity.getCarbohydrates();
        this.alcohol = dietRecordEntity.getAlcohol();
        this.calories = dietRecordEntity.getCalories();
        this.volume = dietRecordEntity.getVolume();
        this.date = dietRecordEntity.getDate();
        this.time = dietRecordEntity.getTime();

        this.createdDate = dietRecordEntity.getCreatedDate();
        this.updatedDate = dietRecordEntity.getUpdatedDate();
    }
}
