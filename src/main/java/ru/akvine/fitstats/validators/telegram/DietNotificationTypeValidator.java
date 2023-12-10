package ru.akvine.fitstats.validators.telegram;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.enums.telegram.DietNotificationSubscriptionType;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.validation.ValidationException;
import ru.akvine.fitstats.validators.Validator;

@Component
public class DietNotificationTypeValidator implements Validator<String> {
    @Override
    public void validate(String type) {
        if (StringUtils.isBlank(type)) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Telegram.DIET_NOTIFICATION_TYPE_BLANK_ERROR,
                    "Telegram diet notification type is blank. Try to input correct value");
        }
        try {
            DietNotificationSubscriptionType.valueOf(type);
        } catch (Exception exception) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Telegram.DIET_NOTIFICATION_TYPE_INVALID_ERROR,
                    "Telegram diet notification type is invalid. Try to input correct value");
        }
    }
}
