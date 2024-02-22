package ru.akvine.fitstats.controllers.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.akvine.fitstats.constants.MessageResolverCodes;
import ru.akvine.fitstats.context.ClientSettingsContext;
import ru.akvine.fitstats.controllers.telegram.converters.TelegramDietNotificationConverter;
import ru.akvine.fitstats.controllers.telegram.dto.common.TelegramBaseRequest;
import ru.akvine.fitstats.controllers.telegram.dto.notification.diet.AddDietNotificationRequest;
import ru.akvine.fitstats.controllers.telegram.dto.notification.diet.DeleteDietNotificationRequest;
import ru.akvine.fitstats.controllers.telegram.validators.TelegramDietNotificationValidator;
import ru.akvine.fitstats.enums.Language;
import ru.akvine.fitstats.services.MessageResolveService;
import ru.akvine.fitstats.services.dto.telegram.AddDietNotificationSubscription;
import ru.akvine.fitstats.services.dto.telegram.DeleteDietNotificationSubscription;
import ru.akvine.fitstats.services.dto.telegram.ListDietNotificationSubscriptions;
import ru.akvine.fitstats.services.telegram.TelegramDietNotificationSubscriptionService;

@Component
@RequiredArgsConstructor
public class TelegramDietNotificationSubscriptionResolver {
    private final TelegramDietNotificationValidator telegramDietNotificationValidator;
    private final TelegramDietNotificationConverter telegramDietNotificationConverter;
    private final TelegramDietNotificationSubscriptionService telegramDietNotificationSubscriptionService;
    private final MessageResolveService messageResolveService;

    public SendMessage list(TelegramBaseRequest request) {
        ListDietNotificationSubscriptions subscriptions = telegramDietNotificationSubscriptionService.list(request);
        return telegramDietNotificationConverter.convertToTelegramListDietNotificationRequest(
                request.getChatId(),
                subscriptions.getSubscriptions());
    }

    public SendMessage add(AddDietNotificationRequest addDietNotificationRequest) {
        telegramDietNotificationValidator.verifyTelegramAddDietNotificationRequest(addDietNotificationRequest);
        AddDietNotificationSubscription addDietNotificationSubscription = telegramDietNotificationConverter.convertToAddDietNotification(addDietNotificationRequest);
        telegramDietNotificationSubscriptionService.add(addDietNotificationSubscription);
        Language language = ClientSettingsContext.getClientSettingsContextHolder().getBySessionForCurrent().getLanguage();
        return new SendMessage(
                addDietNotificationRequest.getChatId(),
                messageResolveService.message(MessageResolverCodes.TELEGRAM_SUBSCRIPTION_ADDED_CODE, language, addDietNotificationSubscription.getType())
        );
    }

    public SendMessage delete(DeleteDietNotificationRequest deleteDietNotificationRequest) {
        telegramDietNotificationValidator.verifyTelegramDeleteDietNotificationRequest(deleteDietNotificationRequest);
        DeleteDietNotificationSubscription deleteDietNotificationSubscription = telegramDietNotificationConverter.convertToDeleteDietNotificationSubscription(deleteDietNotificationRequest);
        Language language = ClientSettingsContext.getClientSettingsContextHolder().getBySessionForCurrent().getLanguage();
        telegramDietNotificationSubscriptionService.delete(deleteDietNotificationSubscription);
        return new SendMessage(
                deleteDietNotificationRequest.getChatId(),
                messageResolveService.message(MessageResolverCodes.TELEGRAM_SUBSCRIPTION_DELETED_CODE, language, deleteDietNotificationSubscription.getType())
        );
    }
}
