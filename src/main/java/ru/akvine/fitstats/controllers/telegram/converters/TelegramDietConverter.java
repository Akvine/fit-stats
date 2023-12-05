package ru.akvine.fitstats.controllers.telegram.converters;

import com.google.common.base.Preconditions;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.akvine.fitstats.controllers.telegram.dto.diet.TelegramDietAddRecord;
import ru.akvine.fitstats.controllers.telegram.dto.diet.TelegramDietDisplay;
import ru.akvine.fitstats.services.dto.diet.AddDietRecordFinish;
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

    public SendMessage convertToAddDietRecordFinishResponse(String chatId, AddDietRecordFinish addDietRecordFinish) {
        Preconditions.checkNotNull(addDietRecordFinish, "addDietRecordFinish is null");

        StringBuilder sb = new StringBuilder();
        sb.append("Запись была успешно добавлена!").append(NEXT_LINE);

        sb.append("Калорий: ").append(MathUtils.round(addDietRecordFinish.getCalories())).append(NEXT_LINE);;
        sb.append("Белки: ").append(MathUtils.round(addDietRecordFinish.getProteins())).append(NEXT_LINE);;
        sb.append("Жиры: ").append(MathUtils.round(addDietRecordFinish.getFats())).append(NEXT_LINE);;
        sb.append("Углеводы: ").append(MathUtils.round(addDietRecordFinish.getCarbohydrates())).append(NEXT_LINE);;
        sb.append("Объем: ").append(MathUtils.round(addDietRecordFinish.getVolume())).append(NEXT_LINE);;

        return new SendMessage(chatId, sb.toString());
    }

    private String buildDietDisplayStatistic(DietDisplay dietDisplay) {
        StringBuilder sb = new StringBuilder();

        sb.append("=======[Оставшиеся]=======").append(NEXT_LINE);
        sb.append("1. Калории: ").append(MathUtils.round(dietDisplay.getRemainingCalories())).append(NEXT_LINE);
        sb.append("2. Белки: ").append(MathUtils.round(dietDisplay.getRemainingProteins())).append(NEXT_LINE);
        sb.append("3. Жиры: ").append(MathUtils.round(dietDisplay.getRemainingFats())).append(NEXT_LINE);
        sb.append("4. Углеводы: ").append(MathUtils.round(dietDisplay.getRemainingCarbohydrates())).append(NEXT_LINE);
        sb.append("=======[Потреблено]=======").append(NEXT_LINE);
        sb.append("1. Калорий: ").append(MathUtils.round(dietDisplay.getCurrentCalories())).append(NEXT_LINE);
        sb.append("2. Белка: ").append(MathUtils.round(dietDisplay.getCurrentProteins())).append(NEXT_LINE);
        sb.append("3. Жиров: ").append(MathUtils.round(dietDisplay.getCurrentFats())).append(NEXT_LINE);
        sb.append("4. Углеводов: ").append(MathUtils.round(dietDisplay.getCurrentCarbohydrates())).append(NEXT_LINE);
        sb.append("=======[Максимум]=======").append(NEXT_LINE);
        sb.append("1. Калорий: ").append(MathUtils.round(dietDisplay.getMaxCalories())).append(NEXT_LINE);
        sb.append("2. Белка: ").append(MathUtils.round(dietDisplay.getMaxProteins())).append(NEXT_LINE);
        sb.append("3. Жиров: ").append(MathUtils.round(dietDisplay.getMaxFats())).append(NEXT_LINE);
        sb.append("4. Углеводов: ").append(MathUtils.round(dietDisplay.getMaxCarbohydrates())).append(NEXT_LINE);

        sb.append("======================");
        return sb.toString();
    }
}
