package ru.akvine.fitstats.services.processors.macronutrient;

import org.springframework.stereotype.Component;
import ru.akvine.fitstats.entities.DietRecordEntity;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CarbohydrateProcessor implements MacronutrientProcessor {
    @Override
    public List<Double> extract(List<DietRecordEntity> records) {
        return records.stream().map(DietRecordEntity::getCarbohydrates).collect(Collectors.toList());
    }

    @Override
    public String getType() {
        return "carbohydrates";
    }
}
