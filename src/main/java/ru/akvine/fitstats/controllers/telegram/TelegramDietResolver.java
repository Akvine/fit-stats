package ru.akvine.fitstats.controllers.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.akvine.fitstats.controllers.rest.dto.diet.ListRecordRequest;
import ru.akvine.fitstats.controllers.telegram.converters.TelegramDietConverter;
import ru.akvine.fitstats.controllers.telegram.dto.common.TelegramBaseRequest;
import ru.akvine.fitstats.controllers.telegram.dto.diet.TelegramDietAddRecord;
import ru.akvine.fitstats.controllers.telegram.dto.diet.TelegramDietDisplay;
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

    public SendMessage display(TelegramDietDisplay telegramDietDisplay) {
        Display display = telegramDietConverter.convertToDisplay(telegramDietDisplay);
        DietDisplay dietDisplay = dietService.display(display);
        return telegramDietConverter.convertToDietDisplayResponse(telegramDietDisplay.getChatId(), dietDisplay);
    }

    public SendMessage addRecord(TelegramDietAddRecord telegramDietAddRecord) {
        telegramDietValidator.verifyTelegramDietAddRecord(telegramDietAddRecord);
        AddDietRecordStart addDietRecordStart = telegramDietConverter.convertToAddDietRecordStart(telegramDietAddRecord);
        AddDietRecordFinish addDietRecordFinish = dietService.add(addDietRecordStart);
        return telegramDietConverter.convertToAddDietRecordFinishResponse(telegramDietAddRecord.getChatId(), addDietRecordFinish);
    }

    public SendMessage listRecord(TelegramBaseRequest request) {
        ListRecord listRecord = new ListRecord().setClientUuid(request.getClientUuid());
        List<DietRecordBean> recordBeans = dietService.list(listRecord);
        return telegramDietConverter.convertToListRecordResponse(request.getChatId(), recordBeans);
    }
}
