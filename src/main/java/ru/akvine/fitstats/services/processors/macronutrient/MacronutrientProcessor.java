package ru.akvine.fitstats.services.processors.macronutrient;

import ru.akvine.fitstats.entities.DietRecordEntity;
import ru.akvine.fitstats.enums.Macronutrient;

import java.util.List;

public interface MacronutrientProcessor {
    List<Double> extract(List<DietRecordEntity> records);
    Macronutrient getType();
}
