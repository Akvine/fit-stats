package ru.akvine.fitstats.controllers.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.akvine.fitstats.controllers.telegram.converters.TelegramStatisticConverter;
import ru.akvine.fitstats.controllers.telegram.dto.statistic.TelegramStatisticHistoryRequest;
import ru.akvine.fitstats.controllers.telegram.validators.TelegramStatisticValidator;
import ru.akvine.fitstats.services.StatisticService;
import ru.akvine.fitstats.services.dto.statistic.StatisticHistory;
import ru.akvine.fitstats.services.dto.statistic.StatisticHistoryResult;

@Component
@RequiredArgsConstructor
public class TelegramStatisticResolver {
    private final StatisticService statisticService;
    private final TelegramStatisticConverter telegramStatisticConverter;
    private final TelegramStatisticValidator telegramStatisticValidator;

    public SendMessage history(TelegramStatisticHistoryRequest request) {
        telegramStatisticValidator.verifyTelegramStatisticHistoryRequest(request);
        StatisticHistory statisticHistory = telegramStatisticConverter.convertToStatisticHistory(request);
        StatisticHistoryResult statisticHistoryResult = statisticService.statisticHistoryInfo(statisticHistory);
        return telegramStatisticConverter.convertToStatisticHistoryResponse(request.getChatId(), statisticHistoryResult);
    }
}
