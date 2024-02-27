package ru.akvine.fitstats.controllers.telegram.parser;

import com.google.common.base.Preconditions;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.telegram.dto.diet.TelegramDietAddRecordByBarCodeRequest;
import ru.akvine.fitstats.exceptions.telegram.parse.TelegramVolumeParseException;

@Component
public class TelegramDietParser {
    public TelegramDietAddRecordByBarCodeRequest parseToTelegramDietAddRecordByBarCodeRequest(String chatId,
                                                                                              String clientUuid,
                                                                                              String caption,
                                                                                              byte[] photo) {
        Preconditions.checkNotNull(clientUuid, "clientUuid null");
        Preconditions.checkNotNull(photo, "photo null");
        Preconditions.checkNotNull(caption, "caption is null");
        Preconditions.checkNotNull(chatId, "chatId is null");

        double volume;
        try {
            volume = Double.parseDouble(caption);
        } catch (Exception exception) {
            throw new TelegramVolumeParseException("Error while parsing volume, ex = " + exception.getMessage());
        }

        return (TelegramDietAddRecordByBarCodeRequest) new TelegramDietAddRecordByBarCodeRequest()
                .setPhoto(photo)
                .setVolume(volume)
                .setClientUuid(clientUuid)
                .setChatId(chatId);
    }
}
