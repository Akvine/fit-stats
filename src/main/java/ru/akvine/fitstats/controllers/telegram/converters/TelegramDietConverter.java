package ru.akvine.fitstats.controllers.telegram.converters;

import com.google.common.base.Preconditions;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.akvine.fitstats.controllers.telegram.dto.common.TelegramBaseRequest;
import ru.akvine.fitstats.controllers.telegram.dto.diet.TelegramDietAddRecord;
import ru.akvine.fitstats.controllers.telegram.dto.diet.TelegramDietDisplay;
import ru.akvine.fitstats.services.dto.diet.*;
import ru.akvine.fitstats.utils.MathUtils;

import java.time.LocalDate;
import java.util.List;

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

    public ListRecord convertToListRecord(TelegramBaseRequest request) {
        Preconditions.checkNotNull(request, "telegramBaseRequest is null");
        return new ListRecord()
                .setDate(LocalDate.now())
                .setClientUuid(request.getClientUuid());
    }

    public SendMessage convertToListRecordResponse(String chatId, List<DietRecordBean> records) {
        Preconditions.checkNotNull("dietRecords is null");
        return new SendMessage(chatId,
                buildListRecordResponse(records));
    }

    private String buildListRecordResponse(List<DietRecordBean> records) {
        int roundAccuracy = 2;

        StringBuilder sb = new StringBuilder();
        sb.append("=======[Список записей]=======").append(NEXT_LINE);

        int size = records.size();
        int lastElementIndex = records.size() - 1;
        for (int i = 0; i < size; ++i) {
            sb.append("--------------------").append(NEXT_LINE);
            sb.append("1. UUID: ").append(records.get(i).getUuid()).append(NEXT_LINE);
            sb.append("2. UUID продукта: ").append(records.get(i).getProductBean().getUuid()).append(NEXT_LINE);
            sb.append("3. Название продукта: ").append(records.get(i).getProductBean().getTitle()).append(NEXT_LINE);
            sb.append("4. Белка: ").append(MathUtils.round(records.get(i).getProteins(), roundAccuracy)).append(NEXT_LINE);
            sb.append("5. Жиров: ").append(MathUtils.round(records.get(i).getFats(), roundAccuracy)).append(NEXT_LINE);
            sb.append("6. Углеводов: ").append(MathUtils.round(records.get(i).getCarbohydrates(), roundAccuracy)).append(NEXT_LINE);
            sb.append("7. Калории: ").append(MathUtils.round(records.get(i).getCalories(), roundAccuracy)).append(NEXT_LINE);
            sb.append("8. Единица измерения: ").append(records.get(i).getProductBean().getMeasurement()).append(NEXT_LINE);
            sb.append("9. Объем: ").append(MathUtils.round(records.get(i).getVolume(), roundAccuracy)).append(NEXT_LINE);
            if (i == lastElementIndex) {
                sb.append("======================");
            }
        }

        return sb.toString();
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
