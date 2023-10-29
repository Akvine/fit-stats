package ru.akvine.fitstats.services.processors.macronutrient;

import ru.akvine.fitstats.entities.DietRecordEntity;

import java.util.List;

public interface MacronutrientProcessor {
    List<Double> extract(List<DietRecordEntity> records);
    String getType();
}
