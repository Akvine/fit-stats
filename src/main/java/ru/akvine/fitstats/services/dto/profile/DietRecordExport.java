package ru.akvine.fitstats.services.dto.profile;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import ru.akvine.fitstats.context.ClientSettingsContext;
import ru.akvine.fitstats.enums.VolumeMeasurement;
import ru.akvine.fitstats.services.dto.diet.DietRecordBean;
import ru.akvine.fitstats.utils.MathUtils;

import java.time.LocalDate;
import java.time.LocalTime;

import static ru.akvine.fitstats.utils.MathUtils.round;

@Data
@Accessors(chain = true)
public class DietRecordExport {
    private double proteins;
    private double fats;
    private double carbohydrates;
    private double calories;
    private double vol;
    private double alcohol;
    private double volume;
    private String product;
    private String uuid;
    private VolumeMeasurement measurement;
    private LocalDate date;
    @Nullable
    private LocalTime time;

    public DietRecordExport(DietRecordBean dietRecordBean) {
        int roundAccuracy = ClientSettingsContext.getClientSettingsContextHolder().getBySessionForCurrent().getRoundAccuracy();
        this.proteins = round(dietRecordBean.getProteins(), roundAccuracy);
        this.fats = round(dietRecordBean.getFats(), roundAccuracy);
        this.carbohydrates = round(dietRecordBean.getCarbohydrates(), roundAccuracy);
        this.calories = round(dietRecordBean.getCalories(), roundAccuracy);
        this.vol = round(dietRecordBean.getVol(), roundAccuracy);
        this.alcohol = round(dietRecordBean.getAlcohol(), roundAccuracy);
        this.volume = dietRecordBean.getVolume();
        this.measurement = dietRecordBean.getProductBean().getMeasurement();
        this.product = dietRecordBean.getProductBean().getTitle();
        this.uuid = dietRecordBean.getProductBean().getUuid();
        this.date = dietRecordBean.getDate();
        this.time = dietRecordBean.getTime();
    }
}
