package ru.akvine.fitstats.controllers.telegram.converters;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.akvine.fitstats.controllers.telegram.dto.common.TelegramBaseRequest;
import ru.akvine.fitstats.controllers.telegram.dto.statistic.TelegramStatisticHistoryRequest;
import ru.akvine.fitstats.enums.Duration;
import ru.akvine.fitstats.enums.Macronutrient;
import ru.akvine.fitstats.services.dto.statistic.AdditionalStatistic;
import ru.akvine.fitstats.services.dto.statistic.AdditionalStatisticInfo;
import ru.akvine.fitstats.services.dto.statistic.StatisticHistory;
import ru.akvine.fitstats.services.dto.statistic.StatisticHistoryResult;
import ru.akvine.fitstats.utils.DateUtils;
import ru.akvine.fitstats.utils.MathUtils;

import java.util.List;

@Component
public class TelegramStatisticConverter {
    private static final String NEXT_LINE = "\n";

    @Value("${processors.supported.indicators}")
    private List<String> supportedIndicators;
    @Value("${processors.supported.macronutrients}")
    private List<String> supportedMacronutrients;
    @Value("${round.accuracy}")
    private int defaultRoundAccuracy;

    public StatisticHistory convertToStatisticHistory(TelegramStatisticHistoryRequest telegramStatisticHistoryRequest) {
        Preconditions.checkNotNull(telegramStatisticHistoryRequest, "telegramStatisticHistoryRequest is null");
        return new StatisticHistory()
                .setClientUuid(telegramStatisticHistoryRequest.getClientUuid())
                .setMacronutrient(Macronutrient.valueOf(telegramStatisticHistoryRequest.getMacronutrient()))
                .setDuration(Duration.valueOf(telegramStatisticHistoryRequest.getDuration()));
    }

    public SendMessage convertToStatisticHistoryResponse(String chatId, StatisticHistoryResult statisticHistoryResult) {
        Preconditions.checkNotNull(statisticHistoryResult, "statisticHistoryResult is null");
        return new SendMessage(
                chatId,
                buildStatisticHistoryResponse(statisticHistoryResult)
        );
    }

    public AdditionalStatistic convertToAdditionalStatistic(TelegramBaseRequest request) {
        Preconditions.checkNotNull(request, "telegramBaseRequest is null");
        int withoutMode = 0;
        return AdditionalStatistic.builder()
                .modeCount(withoutMode)
                .clientUuid(request.getClientUuid())
                .roundAccuracy(defaultRoundAccuracy)
                .dateRange(DateUtils.getYearRange())
                .build();
    }

    public SendMessage convertToAdditionalStatisticResponse(String chatId, AdditionalStatisticInfo additionalStatisticInfo) {
        Preconditions.checkNotNull(additionalStatisticInfo, "additionalStatisticInfo is null");
        return new SendMessage(
                chatId,
                buildAdditionalStatisticResponse(additionalStatisticInfo)
        );
    }

    private String buildStatisticHistoryResponse(StatisticHistoryResult statisticHistoryResult) {
        StringBuilder sb = new StringBuilder();
        sb.append("Период: ").append(statisticHistoryResult.getDuration()).append(NEXT_LINE);
        sb.append("Макронутриент: ").append(statisticHistoryResult.getMacronutrient()).append(NEXT_LINE);
        sb.append("Среднее: ").append(MathUtils.round(statisticHistoryResult.getAverage(), 2)).append(NEXT_LINE);

        statisticHistoryResult
                .getHistory()
                .forEach((key, value) -> {
                   sb.append(key).append(": ").append(value).append(NEXT_LINE);
                });

        return sb.toString();
    }

    private String buildAdditionalStatisticResponse(AdditionalStatisticInfo additionalStatisticInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append("Процент жиров в рационе: ").append(additionalStatisticInfo.getMacronutrientsPercent().get("fats")).append(NEXT_LINE);
        sb.append("Процент белка в рационе: ").append(additionalStatisticInfo.getMacronutrientsPercent().get("proteins")).append(NEXT_LINE);
        sb.append("Процент углеводов в рационе: ").append(additionalStatisticInfo.getMacronutrientsPercent().get("carbohydrates")).append(NEXT_LINE);
        sb.append("Процент калорий от алкоголя в рационе: ").append(additionalStatisticInfo.getMacronutrientsPercent().get("alcohol")).append(NEXT_LINE);
        return sb.toString();
    }
}
