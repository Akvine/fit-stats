package ru.akvine.fitstats.services.processors.macronutrient;

import org.springframework.stereotype.Component;
import ru.akvine.fitstats.entities.DietRecordEntity;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProteinProcessor implements MacronutrientProcessor {
    @Override
    public List<Double> extract(List<DietRecordEntity> records) {
        return records.stream().map(DietRecordEntity::getProteins).collect(Collectors.toList());
    }

    @Override
    public String getType() {
        return "proteins";
    }
}
