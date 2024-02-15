package ru.akvine.fitstats.services.dto.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class DietStatisticHistory {
    private String date;
    private double proteins;
    private double fats;
    private double carbohydrates;
    private double alcohol;
    private double calories;
}
