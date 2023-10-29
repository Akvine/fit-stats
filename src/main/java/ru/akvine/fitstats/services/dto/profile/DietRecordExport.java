package ru.akvine.fitstats.services.dto.profile;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import ru.akvine.fitstats.enums.VolumeMeasurement;
import ru.akvine.fitstats.services.dto.diet.DietRecordBean;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Accessors(chain = true)
public class DietRecordExport {
    private double proteins;
    private double fats;
    private double carbohydrates;
    private double calories;
    private double volume;
    private String product;
    private VolumeMeasurement measurement;
    private LocalDate date;
    @Nullable
    private LocalTime time;

    public DietRecordExport(DietRecordBean dietRecordBean) {
        this.proteins = dietRecordBean.getProteins();
        this.fats = dietRecordBean.getFats();
        this.carbohydrates = dietRecordBean.getCarbohydrates();
        this.calories = dietRecordBean.getCalories();
        this.volume = dietRecordBean.getVolume();
        this.measurement = dietRecordBean.getProductBean().getMeasurement();
        this.product = dietRecordBean.getProductBean().getTitle();
        this.date = dietRecordBean.getDate();
        this.time = dietRecordBean.getTime();
    }
}
