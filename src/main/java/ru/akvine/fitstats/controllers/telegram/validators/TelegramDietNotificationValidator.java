package ru.akvine.fitstats.controllers.telegram.validators;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.telegram.dto.notification.diet.AddDietNotificationRequest;
import ru.akvine.fitstats.controllers.telegram.dto.notification.diet.DeleteDietNotificationRequest;
import ru.akvine.fitstats.enums.telegram.DietNotificationSubscriptionType;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.validation.ValidationException;
import ru.akvine.fitstats.services.client.ClientService;
import ru.akvine.fitstats.services.telegram.TelegramDietNotificationSubscriptionService;
import ru.akvine.fitstats.validators.telegram.DietNotificationTypeValidator;

@Component
@RequiredArgsConstructor
public class TelegramDietNotificationValidator {
    private final DietNotificationTypeValidator dietNotificationTypeValidator;
    private final TelegramDietNotificationSubscriptionService telegramDietNotificationSubscriptionService;
    private final ClientService clientService;

    public void verifyTelegramAddDietNotificationRequest(AddDietNotificationRequest addDietNotificationRequest) {
        Preconditions.checkNotNull(addDietNotificationRequest, "telegramAddDietNotification is null");

        String type = addDietNotificationRequest.getType().toUpperCase();
        dietNotificationTypeValidator.validate(type);

        Long clientId = clientService.getByUuid(addDietNotificationRequest.getClientUuid()).getId();
        verifyTypeNotExistsForClient(clientId, DietNotificationSubscriptionType.valueOf(type));
    }

    public void verifyTelegramDeleteDietNotificationRequest(DeleteDietNotificationRequest deleteDietNotificationRequest) {
        Preconditions.checkNotNull(deleteDietNotificationRequest, "deleteDietNotificationRequest is null");

        String type = deleteDietNotificationRequest.getType();
        dietNotificationTypeValidator.validate(type);

        Long clientId = clientService.getByUuid(deleteDietNotificationRequest.getClientUuid()).getId();
        verifyTypeExistsForClient(clientId, DietNotificationSubscriptionType.valueOf(type));
    }

    private void verifyTypeNotExistsForClient(Long clientId, DietNotificationSubscriptionType type) {
        if (telegramDietNotificationSubscriptionService.isExistsByClientIdAndType(clientId, type)) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Telegram.DIET_NOTIFICATION_TYPE_EXISTS_FOR_CLIENT_ERROR,
                    "Type = [" + type + "] already exists for client!");
        }
    }

    private void verifyTypeExistsForClient(Long clientId, DietNotificationSubscriptionType type) {
        if (!telegramDietNotificationSubscriptionService.isExistsByClientIdAndType(clientId, type)) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.Telegram.DIET_NOTIFICATION_TYPE_NOT_EXISTS_FOR_CLIENT_ERROR,
                    "Type = [" + type + "] not exists for client!");
        }
    }
}
