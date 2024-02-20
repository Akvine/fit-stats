package ru.akvine.fitstats.controllers.telegram.converters;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.akvine.fitstats.constants.MessageResolverCodes;
import ru.akvine.fitstats.context.ClientSettingsContext;
import ru.akvine.fitstats.controllers.telegram.dto.common.TelegramBaseRequest;
import ru.akvine.fitstats.controllers.telegram.dto.diet.TelegramDietAddRecordRequest;
import ru.akvine.fitstats.controllers.telegram.dto.diet.TelegramDietDeleteRecordRequest;
import ru.akvine.fitstats.controllers.telegram.dto.diet.TelegramDietDisplayRequest;
import ru.akvine.fitstats.enums.Language;
import ru.akvine.fitstats.enums.telegram.PrintMacronutrientsMode;
import ru.akvine.fitstats.services.MessageResolveService;
import ru.akvine.fitstats.services.dto.client.ClientSettingsBean;
import ru.akvine.fitstats.services.dto.diet.*;
import ru.akvine.fitstats.services.dto.telegram.diet.PrintMacronutrientsService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static ru.akvine.fitstats.utils.MathUtils.round;

@Component
@RequiredArgsConstructor
public class TelegramDietConverter {
    private final static String NEXT_LINE = "\n";
    private final static String EMPTY_SPACE = " ";
    private final static String NON_EMPTY_SPACE = "";
    private final static String COMMA = ",";

    private final Map<PrintMacronutrientsMode, PrintMacronutrientsService> availablePrintMacronutrientsService;
    private final MessageResolveService messageResolveService;

    @Autowired
    public TelegramDietConverter(List<PrintMacronutrientsService> printMacronutrientsServices,
                                 MessageResolveService messageResolveService) {
        this.messageResolveService = messageResolveService;
        this.availablePrintMacronutrientsService = printMacronutrientsServices
                .stream()
                .collect(toMap(PrintMacronutrientsService::getType, identity()));
    }

    public Display convertToDisplay(TelegramDietDisplayRequest telegramDietDisplayRequest) {
        Preconditions.checkNotNull(telegramDietDisplayRequest, "telegramDietDisplay is null");
        return new Display()
                .setClientUuid(telegramDietDisplayRequest.getClientUuid());
    }

    public SendMessage convertToDietDisplayResponse(String chatId,
                                                    DietDisplay dietDisplay) {
        Preconditions.checkNotNull(dietDisplay, "dietDisplay is null");
        return new SendMessage(
                chatId,
                buildDietDisplayStatistic(dietDisplay)
        );
    }

    public AddDietRecordStart convertToAddDietRecordStart(TelegramDietAddRecordRequest telegramDietAddRecordRequest) {
        Preconditions.checkNotNull(telegramDietAddRecordRequest, "telegramDietAddRecord is null");
        String text = telegramDietAddRecordRequest.getText();
        String[] parts = text
                .trim()
                .replaceAll(EMPTY_SPACE, NON_EMPTY_SPACE)
                .split(COMMA);
        String productUuid = parts[0];
        double volume = Double.parseDouble(parts[1]);
        return new AddDietRecordStart()
                .setProductUuid(productUuid)
                .setClientUuid(telegramDietAddRecordRequest.getClientUuid())
                .setVolume(volume)
                .setDate(LocalDate.now());
    }

    public SendMessage convertToAddDietRecordFinishResponse(String chatId,
                                                            AddDietRecordFinish addDietRecordFinish) {
        Preconditions.checkNotNull(addDietRecordFinish, "addDietRecordFinish is null");

        ClientSettingsBean clientSettingsBean = ClientSettingsContext.getClientSettingsContextHolder().getByThreadLocalForCurrent();
        int roundAccuracy = clientSettingsBean.getRoundAccuracy();
        Language language = clientSettingsBean.getLanguage();

        StringBuilder sb = new StringBuilder();
        sb
                .append(messageResolveService.message(MessageResolverCodes.DIET_RECORD_ADD_SUCCESSFUL_CODE, language))
                .append(NEXT_LINE);
        sb
                .append(messageResolveService.message(MessageResolverCodes.CALORIES_CODE, language))
                .append(": ")
                .append(round(addDietRecordFinish.getCalories(), roundAccuracy))
                .append(NEXT_LINE);
        sb
                .append(messageResolveService.message(MessageResolverCodes.PROTEINS_CODE, language))
                .append(": ")
                .append(round(addDietRecordFinish.getProteins(), roundAccuracy))
                .append(NEXT_LINE);
        sb
                .append(messageResolveService.message(MessageResolverCodes.FATS_CODE, language))
                .append(": ")
                .append(round(addDietRecordFinish.getFats(), roundAccuracy))
                .append(NEXT_LINE);
        sb
                .append(messageResolveService.message(MessageResolverCodes.CARBOHYDRATES_CODE, language))
                .append(": ")
                .append(round(addDietRecordFinish.getCarbohydrates(), roundAccuracy))
                .append(NEXT_LINE);
        sb
                .append(messageResolveService.message(messageResolveService.message(MessageResolverCodes.VOLUME_CODE, language)))
                .append(": ")
                .append(round(addDietRecordFinish.getVolume(), roundAccuracy))
                .append(NEXT_LINE);

        return new SendMessage(chatId, sb.toString());
    }

    public ListRecordsStart convertToListRecord(TelegramBaseRequest request) {
        Preconditions.checkNotNull(request, "telegramBaseRequest is null");
        return new ListRecordsStart()
                .setDate(LocalDate.now())
                .setClientUuid(request.getClientUuid());
    }

    public SendMessage convertToListRecordResponse(String chatId,
                                                   ListRecordsFinish listRecordsFinish) {
        Preconditions.checkNotNull(listRecordsFinish, "listRecordsFinish is null");
        return new SendMessage(chatId,
                buildListRecordResponse(listRecordsFinish.getRecords()));
    }

    public DeleteRecord convertToDeleteRecord(TelegramDietDeleteRecordRequest request) {
        Preconditions.checkNotNull(request, "telegramDietDeleteRecordRequest is null");
        return new DeleteRecord()
                .setRecordUuid(request.getUuid())
                .setClientUuid(request.getClientUuid());
    }

    private String buildListRecordResponse(List<DietRecordBean> records) {
        ClientSettingsBean clientSettingsBean = ClientSettingsContext.getClientSettingsContextHolder().getByThreadLocalForCurrent();
        int roundAccuracy = clientSettingsBean.getRoundAccuracy();
        Language language = clientSettingsBean.getLanguage();

        StringBuilder sb = new StringBuilder();
        sb.append("=======[");
        sb.append(messageResolveService.message(MessageResolverCodes.DIET_RECORD_ADD_SUCCESSFUL_CODE, language));
        sb.append("]=======").append(NEXT_LINE);

        int size = records.size();
        int lastElementIndex = records.size() - 1;
        for (int i = 0; i < size; ++i) {
            sb.append("--------------------").append(NEXT_LINE);
            sb.append("1. UUID: ").append(records.get(i).getUuid()).append(NEXT_LINE);
            sb.append("2. ")
                    .append(messageResolveService.message(MessageResolverCodes.PRODUCT_UUID_CODE, language))
                    .append(": ")
                    .append(records.get(i).getProductBean().getUuid())
                    .append(NEXT_LINE);
            sb.append("3. ")
                    .append(messageResolveService.message(MessageResolverCodes.PRODUCT_NAME_CODE, language))
                    .append(": ")
                    .append(records.get(i).getProductBean().getTitle())
                    .append(NEXT_LINE);
            sb.append("4. ")
                    .append(messageResolveService.message(MessageResolverCodes.CALORIES_CODE, language))
                    .append(": ")
                    .append(round(records.get(i).getCalories(), roundAccuracy))
                    .append(NEXT_LINE);
            sb.append("5. ")
                    .append(messageResolveService.message(MessageResolverCodes.PROTEINS_CODE, language))
                    .append(": ")
                    .append(round(records.get(i).getProteins(), roundAccuracy))
                    .append(NEXT_LINE);
            sb.append("6.")
                    .append(messageResolveService.message(MessageResolverCodes.FATS_CODE, language))
                    .append(": ")
                    .append(round(records.get(i).getFats(), roundAccuracy))
                    .append(NEXT_LINE);
            sb.append("7. ")
                    .append(messageResolveService.message(MessageResolverCodes.CARBOHYDRATES_CODE, language))
                    .append(": ")
                    .append(round(records.get(i).getCalories(), roundAccuracy))
                    .append(NEXT_LINE);
            sb.append("8. ")
                    .append(messageResolveService.message(MessageResolverCodes.MEASUREMENT_TYPE_CODE, language))
                    .append(": ")
                    .append(records.get(i).getProductBean().getMeasurement())
                    .append(NEXT_LINE);
            sb.append("9. ")
                    .append(messageResolveService.message(MessageResolverCodes.VOLUME_CODE, language))
                    .append(": ")
                    .append(round(records.get(i).getVolume(), roundAccuracy))
                    .append(NEXT_LINE);
            if (i == lastElementIndex) {
                sb.append("======================");
            }
        }

        return sb.toString();
    }

    private String buildDietDisplayStatistic(DietDisplay dietDisplay) {
        ClientSettingsBean clientSettingsBean = ClientSettingsContext.getClientSettingsContextHolder().getByThreadLocalForCurrent();
        return availablePrintMacronutrientsService
                .get(clientSettingsBean.getPrintMacronutrientsMode())
                .print(dietDisplay, clientSettingsBean);
    }
}
