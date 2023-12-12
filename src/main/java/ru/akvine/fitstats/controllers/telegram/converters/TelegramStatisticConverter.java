package ru.akvine.fitstats.controllers.telegram.converters;

import com.google.common.base.Preconditions;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.akvine.fitstats.controllers.telegram.dto.statistic.TelegramStatisticHistoryRequest;
import ru.akvine.fitstats.enums.Duration;
import ru.akvine.fitstats.enums.Macronutrient;
import ru.akvine.fitstats.services.dto.statistic.StatisticHistory;
import ru.akvine.fitstats.services.dto.statistic.StatisticHistoryResult;
import ru.akvine.fitstats.utils.MathUtils;

@Component
public class TelegramStatisticConverter {
    private static final String NEXT_LINE = "\n";

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
}
