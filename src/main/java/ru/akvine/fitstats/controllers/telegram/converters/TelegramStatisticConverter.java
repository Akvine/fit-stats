package ru.akvine.fitstats.controllers.telegram.converters;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.akvine.fitstats.constants.MessageResolverCodes;
import ru.akvine.fitstats.context.ClientSettingsContext;
import ru.akvine.fitstats.controllers.telegram.dto.common.TelegramBaseRequest;
import ru.akvine.fitstats.controllers.telegram.dto.statistic.TelegramStatisticHistoryRequest;
import ru.akvine.fitstats.enums.Duration;
import ru.akvine.fitstats.enums.Language;
import ru.akvine.fitstats.enums.Macronutrient;
import ru.akvine.fitstats.services.MessageResolveService;
import ru.akvine.fitstats.services.dto.client.ClientSettingsBean;
import ru.akvine.fitstats.services.dto.statistic.AdditionalStatistic;
import ru.akvine.fitstats.services.dto.statistic.AdditionalStatisticInfo;
import ru.akvine.fitstats.services.dto.statistic.StatisticHistory;
import ru.akvine.fitstats.services.dto.statistic.StatisticHistoryResult;
import ru.akvine.fitstats.utils.DateUtils;

import static ru.akvine.fitstats.utils.MathUtils.round;

@Component
@RequiredArgsConstructor
public class TelegramStatisticConverter {
    private static final String NEXT_LINE = "\n";
    private final MessageResolveService messageResolveService;

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
        int roundAccuracy = ClientSettingsContext.getClientSettingsContextHolder().getBySessionForCurrent().getRoundAccuracy();
        int withoutMode = 0;
        return AdditionalStatistic.builder()
                .modeCount(withoutMode)
                .clientUuid(request.getClientUuid())
                .roundAccuracy(roundAccuracy)
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
        ClientSettingsBean clientSettingsBean = ClientSettingsContext.getClientSettingsContextHolder().getBySessionForCurrent();
        int roundAccuracy = clientSettingsBean.getRoundAccuracy();
        Language language = clientSettingsBean.getLanguage();
        StringBuilder sb = new StringBuilder();
        sb
                .append(messageResolveService.message(MessageResolverCodes.DURATION_CODE, language))
                .append(": ")
                .append(statisticHistoryResult.getDuration()).append(NEXT_LINE);
        sb
                .append(messageResolveService.message(MessageResolverCodes.MACRONUTRIENT_CODE, language))
                .append(": ")
                .append(statisticHistoryResult.getMacronutrient()).append(NEXT_LINE);
        sb
                .append(messageResolveService.message(MessageResolverCodes.AVERAGE_CODE, language))
                .append(": ")
                .append(round(statisticHistoryResult.getAverage(), roundAccuracy)).append(NEXT_LINE);
        sb
                .append(messageResolveService.message(MessageResolverCodes.MEDIAN_CODE, language))
                .append(": ")
                .append(round(statisticHistoryResult.getMedian(), roundAccuracy)).append(NEXT_LINE);

        statisticHistoryResult
                .getHistory()
                .forEach((key, value) -> {
                   sb.append(key).append(": ").append(value).append(NEXT_LINE);
                });

        return sb.toString();
    }

    private String buildAdditionalStatisticResponse(AdditionalStatisticInfo additionalStatisticInfo) {
        Language language = ClientSettingsContext.getClientSettingsContextHolder().getBySessionForCurrent().getLanguage();
        StringBuilder sb = new StringBuilder();
        sb
                .append(messageResolveService.message(MessageResolverCodes.PROTEINS_PERCENT_DIET_CODE, language))
                .append(": ")
                .append(additionalStatisticInfo.getMacronutrientsPercent().get("fats")).append(NEXT_LINE);
        sb
                .append(messageResolveService.message(MessageResolverCodes.FATS_PERCENT_DIET_CODE, language))
                .append(": ")
                .append(additionalStatisticInfo.getMacronutrientsPercent().get("proteins")).append(NEXT_LINE);
        sb
                .append(messageResolveService.message(MessageResolverCodes.CARBOHYDRATES_PERCENT_DIET_CODE, language))
                .append(": ")
                .append(additionalStatisticInfo.getMacronutrientsPercent().get("carbohydrates")).append(NEXT_LINE);
        sb
                .append(messageResolveService.message(MessageResolverCodes.ALCOHOL_PERCENT_DIET_CODE, language))
                .append(": ")
                .append(additionalStatisticInfo.getMacronutrientsPercent().get("alcohol")).append(NEXT_LINE);
        return sb.toString();
    }
}
