package ru.akvine.fitstats.controllers.telegram.validators;

import com.google.common.base.Preconditions;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.telegram.dto.diet.TelegramDietAddRecordRequest;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.validation.ValidationException;

@Component
public class TelegramDietValidator {
    private final static String EMPTY_SPACE = " ";
    private final static String NON_EMPTY_SPACE = "";
    private final static String COMMA = ",";

    public void verifyTelegramDietAddRecordRequest(TelegramDietAddRecordRequest telegramDietAddRecordRequest) {
        Preconditions.checkNotNull(telegramDietAddRecordRequest, "telegramDietAddRecord is null");
        String text = telegramDietAddRecordRequest.getText();
        if (!text.contains(COMMA)) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Telegram.DIET_ADD_RECORD_MESSAGE_INVALID_ERROR,
                    "Diet add record invalid message!"
            );
        }
        String[] parts = text
                .trim()
                .replaceAll(EMPTY_SPACE, NON_EMPTY_SPACE)
                .split(COMMA);
        try {
            Double.parseDouble(parts[1]);
        } catch (NumberFormatException exception) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Telegram.VOLUME_INVALID_ERROR,
                    "Invalid volume!"
            );
        }
    }
}
