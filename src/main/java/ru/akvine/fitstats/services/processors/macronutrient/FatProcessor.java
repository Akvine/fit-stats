package ru.akvine.fitstats.services.processors.macronutrient;

import org.springframework.stereotype.Component;
import ru.akvine.fitstats.entities.DietRecordEntity;
import ru.akvine.fitstats.enums.Macronutrient;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FatProcessor implements MacronutrientProcessor {
    @Override
    public List<Double> extract(List<DietRecordEntity> records) {
        return records.stream().map(DietRecordEntity::getFats).collect(Collectors.toList());
    }

    @Override
    public Macronutrient getType() {
        return Macronutrient.FATS;
    }
}
