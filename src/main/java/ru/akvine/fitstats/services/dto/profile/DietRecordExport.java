package ru.akvine.fitstats.services.dto.profile;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import ru.akvine.fitstats.enums.VolumeMeasurement;
import ru.akvine.fitstats.services.dto.diet.DietRecordBean;
import ru.akvine.fitstats.utils.MathUtils;

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
    private String uuid;
    private VolumeMeasurement measurement;
    private LocalDate date;
    @Nullable
    private LocalTime time;

    public DietRecordExport(DietRecordBean dietRecordBean) {
        this.proteins = MathUtils.round(dietRecordBean.getProteins(), 2);
        this.fats = MathUtils.round(dietRecordBean.getFats(), 2);
        this.carbohydrates = MathUtils.round(dietRecordBean.getCarbohydrates(), 2);
        this.calories = MathUtils.round(dietRecordBean.getCalories(), 2);
        this.volume = dietRecordBean.getVolume();
        this.measurement = dietRecordBean.getProductBean().getMeasurement();
        this.product = dietRecordBean.getProductBean().getTitle();
        this.uuid = dietRecordBean.getProductBean().getUuid();
        this.date = dietRecordBean.getDate();
        this.time = dietRecordBean.getTime();
    }
}
