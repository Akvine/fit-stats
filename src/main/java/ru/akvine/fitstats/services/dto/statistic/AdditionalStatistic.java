package ru.akvine.fitstats.services.dto.statistic;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class AdditionalStatistic extends Statistic {
    private int modeCount;
}
