package ru.akvine.fitstats.controllers.telegram.converters;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.akvine.fitstats.constants.MessageResolverCodes;
import ru.akvine.fitstats.context.ClientSettingsContext;
import ru.akvine.fitstats.controllers.telegram.dto.notification.diet.AddDietNotificationRequest;
import ru.akvine.fitstats.controllers.telegram.dto.notification.diet.DeleteDietNotificationRequest;
import ru.akvine.fitstats.enums.Language;
import ru.akvine.fitstats.enums.telegram.DietNotificationSubscriptionType;
import ru.akvine.fitstats.services.MessageResolveService;
import ru.akvine.fitstats.services.dto.telegram.AddDietNotificationSubscription;
import ru.akvine.fitstats.services.dto.telegram.DeleteDietNotificationSubscription;
import ru.akvine.fitstats.services.dto.telegram.TelegramDietNotificationSubscription;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TelegramDietNotificationConverter {
    private final static String NEXT_LINE = "\n";

    private final MessageResolveService messageResolveService;

    public AddDietNotificationSubscription convertToAddDietNotification(AddDietNotificationRequest request) {
        Preconditions.checkNotNull(request, "addDietNotificationRequest is null");
        return new AddDietNotificationSubscription()
                .setTelegramId(request.getTelegramId())
                .setType(DietNotificationSubscriptionType.valueOf(request.getType().toUpperCase()))
                .setClientUuid(request.getClientUuid());
    }

    public DeleteDietNotificationSubscription convertToDeleteDietNotificationSubscription(DeleteDietNotificationRequest deleteDietNotificationRequest) {
        Preconditions.checkNotNull(deleteDietNotificationRequest, "deleteDietNotificationRequest is null");
        return new DeleteDietNotificationSubscription()
                .setClientUuid(deleteDietNotificationRequest.getClientUuid())
                .setType(DietNotificationSubscriptionType.valueOf(deleteDietNotificationRequest.getType().toUpperCase()));
    }

    public SendMessage convertToTelegramListDietNotificationRequest(String chatId,
                                                                    List<TelegramDietNotificationSubscription> notificationSubscriptions) {
        Preconditions.checkNotNull(notificationSubscriptions, "notificationSubscriptions is null");
        return new SendMessage(
                chatId,
                buildTelegramListDietNotificationRequest(notificationSubscriptions)
        );
    }

    private String buildTelegramListDietNotificationRequest(List<TelegramDietNotificationSubscription> notificationSubscriptions) {
        Language language = ClientSettingsContext.getClientSettingsContextHolder().getByThreadLocalForCurrent().getLanguage();
        StringBuilder sb = new StringBuilder();
        sb
                .append(messageResolveService.message(MessageResolverCodes.DIET_NOTIFICATION_SUBSCRIPTION_ACTIVE_LIST_CODE, language))
                .append(": ")
                .append(NEXT_LINE);

        notificationSubscriptions.forEach(subscription -> {
                    if (subscription.getDietNotificationSubscriptionType() == DietNotificationSubscriptionType.PROTEINS) {
                        sb.append(messageResolveService.message(MessageResolverCodes.DIET_NOTIFICATION_SUBSCRIPTION_PROTEINS_LIMIT_EXCEED_CODE, language))
                                .append(NEXT_LINE);
                    } else if (subscription.getDietNotificationSubscriptionType() == DietNotificationSubscriptionType.FATS) {
                        sb.append(messageResolveService.message(MessageResolverCodes.DIET_NOTIFICATION_SUBSCRIPTION_FATS_LIMIT_EXCEED_CODE, language))
                                .append(NEXT_LINE);
                    } else if (subscription.getDietNotificationSubscriptionType() == DietNotificationSubscriptionType.CARBOHYDRATES) {
                        sb.append(messageResolveService.message(MessageResolverCodes.DIET_NOTIFICATION_SUBSCRIPTION_CARBOHYDRATES_LIMIT_EXCEED_CODE, language))
                                .append(NEXT_LINE);
                    } else if (subscription.getDietNotificationSubscriptionType() == DietNotificationSubscriptionType.CALORIES) {
                        sb.append(messageResolveService.message(MessageResolverCodes.DIET_NOTIFICATION_SUBSCRIPTION_CALORIES_LIMIT_EXCEED_CODE, language))
                                .append(NEXT_LINE);
                    } else {
                        sb.append(messageResolveService.message(MessageResolverCodes.DIET_NOTIFICATION_SUBSCRIPTION_ALL_LIMIT_EXCEED_CODE, language))
                                .append(NEXT_LINE);
                    }
                }
        );

        return sb.toString();
    }
}
