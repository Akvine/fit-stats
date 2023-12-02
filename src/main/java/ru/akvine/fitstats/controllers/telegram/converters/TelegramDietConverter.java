package ru.akvine.fitstats.controllers.telegram.converters;

import com.google.common.base.Preconditions;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.akvine.fitstats.controllers.telegram.dto.diet.TelegramDietAddRecord;
import ru.akvine.fitstats.controllers.telegram.dto.diet.TelegramDietDisplay;
import ru.akvine.fitstats.services.dto.diet.AddDietRecordStart;
import ru.akvine.fitstats.services.dto.diet.DietDisplay;
import ru.akvine.fitstats.services.dto.diet.Display;
import ru.akvine.fitstats.utils.MathUtils;

import java.time.LocalDate;

@Component
public class TelegramDietConverter {
    private final static String NEXT_LINE = "\n";
    private final static String EMPTY_SPACE = " ";
    private final static String NON_EMPTY_SPACE = "";
    private final static String COMMA = ",";

    public Display convertToDisplay(TelegramDietDisplay telegramDietDisplay) {
        Preconditions.checkNotNull(telegramDietDisplay, "telegramDietDisplay is null");
        return new Display()
                .setClientUuid(telegramDietDisplay.getClientUuid());
    }

    public SendMessage convertToDietDisplayResponse(String chatId, DietDisplay dietDisplay) {
        Preconditions.checkNotNull(dietDisplay, "dietDisplay is null");
        return new SendMessage(
                chatId,
                buildDietDisplayStatistic(dietDisplay)
        );
    }

    public AddDietRecordStart convertToAddDietRecordStart(TelegramDietAddRecord telegramDietAddRecord) {
        Preconditions.checkNotNull(telegramDietAddRecord, "telegramDietAddRecord is null");
        String text = telegramDietAddRecord.getText();
        String[] parts = text
                .trim()
                .replaceAll(EMPTY_SPACE, NON_EMPTY_SPACE)
                .split(COMMA);
        String productUuid = parts[0];
        double volume = Double.parseDouble(parts[1]);
        return new AddDietRecordStart()
                .setProductUuid(productUuid)
                .setClientUuid(telegramDietAddRecord.getClientUuid())
                .setVolume(volume)
                .setDate(LocalDate.now());
    }

    private String buildDietDisplayStatistic(DietDisplay dietDisplay) {
        StringBuilder sb = new StringBuilder();
        sb.append("БЖУ на сегодня: ").append(NEXT_LINE);

        sb.append("Максимум калорий: ").append(MathUtils.round(dietDisplay.getMaxCalories())).append(NEXT_LINE);
        sb.append("Максимум белка: ").append(MathUtils.round(dietDisplay.getMaxProteins())).append(NEXT_LINE);
        sb.append("Максимум жиров: ").append(MathUtils.round(dietDisplay.getMaxFats())).append(NEXT_LINE);
        sb.append("Максимум углеводов: ").append(MathUtils.round(dietDisplay.getMaxCarbohydrates())).append(NEXT_LINE);
        sb.append("Потреблено калорий: ").append(MathUtils.round(dietDisplay.getCurrentCalories())).append(NEXT_LINE);
        sb.append("Потреблено белка: ").append(MathUtils.round(dietDisplay.getCurrentProteins())).append(NEXT_LINE);
        sb.append("Потреблено жиров: ").append(MathUtils.round(dietDisplay.getCurrentFats())).append(NEXT_LINE);
        sb.append("Потреблено углеводов: ").append(MathUtils.round(dietDisplay.getCurrentCarbohydrates())).append(NEXT_LINE);
        sb.append("Оставшиеся калорий: ").append(MathUtils.round(dietDisplay.getRemainingCalories())).append(NEXT_LINE);
        sb.append("Оставшиеся белки: ").append(MathUtils.round(dietDisplay.getRemainingProteins())).append(NEXT_LINE);
        sb.append("Оставшиеся жиры: ").append(MathUtils.round(dietDisplay.getRemainingFats())).append(NEXT_LINE);
        sb.append("Оставшиеся углеводы: ").append(MathUtils.round(dietDisplay.getRemainingCarbohydrates())).append(NEXT_LINE);

        sb.append("======================");
        return sb.toString();
    }
}
