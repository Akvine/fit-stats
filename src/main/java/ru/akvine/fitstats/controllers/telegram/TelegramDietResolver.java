package ru.akvine.fitstats.controllers.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.akvine.fitstats.controllers.telegram.converters.TelegramDietConverter;
import ru.akvine.fitstats.controllers.telegram.dto.common.TelegramBaseRequest;
import ru.akvine.fitstats.controllers.telegram.dto.diet.TelegramDietAddRecordRequest;
import ru.akvine.fitstats.controllers.telegram.dto.diet.TelegramDietDeleteRecordRequest;
import ru.akvine.fitstats.controllers.telegram.dto.diet.TelegramDietDisplayRequest;
import ru.akvine.fitstats.controllers.telegram.validators.TelegramDietValidator;
import ru.akvine.fitstats.services.DietService;
import ru.akvine.fitstats.services.dto.diet.*;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TelegramDietResolver {
    private final DietService dietService;
    private final TelegramDietConverter telegramDietConverter;
    private final TelegramDietValidator telegramDietValidator;

    public SendMessage display(TelegramDietDisplayRequest telegramDietDisplayRequest) {
        Display display = telegramDietConverter.convertToDisplay(telegramDietDisplayRequest);
        DietDisplay dietDisplay = dietService.display(display);
        return telegramDietConverter.convertToDietDisplayResponse(telegramDietDisplayRequest.getChatId(), dietDisplay);
    }

    public SendMessage addRecord(TelegramDietAddRecordRequest telegramDietAddRecordRequest) {
        telegramDietValidator.verifyTelegramDietAddRecordRequest(telegramDietAddRecordRequest);
        AddDietRecordStart addDietRecordStart = telegramDietConverter.convertToAddDietRecordStart(telegramDietAddRecordRequest);
        AddDietRecordFinish addDietRecordFinish = dietService.add(addDietRecordStart);
        return telegramDietConverter.convertToAddDietRecordFinishResponse(telegramDietAddRecordRequest.getChatId(), addDietRecordFinish);
    }

    public SendMessage listRecord(TelegramBaseRequest request) {
        ListRecord listRecord = telegramDietConverter.convertToListRecord(request);
        List<DietRecordBean> recordBeans = dietService.list(listRecord);
        return telegramDietConverter.convertToListRecordResponse(request.getChatId(), recordBeans);
    }

    public SendMessage deleteRecord(TelegramDietDeleteRecordRequest request) {
        DeleteRecord deleteRecord = telegramDietConverter.convertToDeleteRecord(request);
        dietService.deleteRecords(deleteRecord);
        return new SendMessage(
                request.getChatId(),
                "Запись успешно удалена!"
        );
    }
}
