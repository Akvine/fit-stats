package ru.akvine.fitstats.controllers.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.akvine.fitstats.controllers.telegram.converters.TelegramDietNotificationConverter;
import ru.akvine.fitstats.controllers.telegram.dto.common.TelegramBaseRequest;
import ru.akvine.fitstats.controllers.telegram.dto.notification.diet.AddDietNotificationRequest;
import ru.akvine.fitstats.controllers.telegram.dto.notification.diet.DeleteDietNotificationRequest;
import ru.akvine.fitstats.controllers.telegram.validators.TelegramDietNotificationValidator;
import ru.akvine.fitstats.services.dto.telegram.AddDietNotificationSubscription;
import ru.akvine.fitstats.services.dto.telegram.DeleteDietNotificationSubscription;
import ru.akvine.fitstats.services.dto.telegram.TelegramDietNotificationSubscription;
import ru.akvine.fitstats.services.telegram.TelegramDietNotificationSubscriptionService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TelegramDietNotificationSubscriptionResolver {
    private final TelegramDietNotificationValidator telegramDietNotificationValidator;
    private final TelegramDietNotificationConverter telegramDietNotificationConverter;
    private final TelegramDietNotificationSubscriptionService telegramDietNotificationSubscriptionService;

    public SendMessage list(TelegramBaseRequest request) {
        List<TelegramDietNotificationSubscription> subscriptions = telegramDietNotificationSubscriptionService.list(request);
        return telegramDietNotificationConverter.convertToTelegramListDietNotificationRequest(request.getChatId(), subscriptions);
    }

    public SendMessage add(AddDietNotificationRequest addDietNotificationRequest) {
        telegramDietNotificationValidator.verifyTelegramAddDietNotificationRequest(addDietNotificationRequest);
        AddDietNotificationSubscription addDietNotificationSubscription = telegramDietNotificationConverter.convertToAddDietNotification(addDietNotificationRequest);
        telegramDietNotificationSubscriptionService.add(addDietNotificationSubscription);
        return new SendMessage(
                addDietNotificationRequest.getChatId(),
                "Отслеживание показателя = [" + addDietNotificationSubscription.getType() + "] было успешно добавлено!"
        );
    }

    public SendMessage delete(DeleteDietNotificationRequest deleteDietNotificationRequest) {
        telegramDietNotificationValidator.verifyTelegramDeleteDietNotificationRequest(deleteDietNotificationRequest);
        DeleteDietNotificationSubscription deleteDietNotificationSubscription = telegramDietNotificationConverter.convertToDeleteDietNotificationSubscription(deleteDietNotificationRequest);
        telegramDietNotificationSubscriptionService.delete(deleteDietNotificationSubscription);
        return new SendMessage(
                deleteDietNotificationRequest.getChatId(),
                "Отслеживание показателя = [" + deleteDietNotificationSubscription.getType() + "] было успешно удалено!"
        );
    }
}
