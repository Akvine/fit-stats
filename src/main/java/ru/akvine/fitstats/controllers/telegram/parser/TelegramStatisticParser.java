package ru.akvine.fitstats.controllers.telegram.parser;

import com.google.common.base.Preconditions;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.telegram.dto.statistic.TelegramStatisticHistoryRequest;

@Component
public class TelegramStatisticParser {
    private static final String COMMA = ",";

    public TelegramStatisticHistoryRequest parseToTelegramStatisticHistoryRequest(String clientUuid, String chatId, String text) {
        Preconditions.checkNotNull(text, "text is null");
        String[] parts = text.split(COMMA);

        String duration = parts[0].trim();
        String macronutrient = parts[1].trim();

        return (TelegramStatisticHistoryRequest) new TelegramStatisticHistoryRequest()
                .setDuration(duration)
                .setMacronutrient(macronutrient)
                .setChatId(chatId)
                .setClientUuid(clientUuid);
    }
}
