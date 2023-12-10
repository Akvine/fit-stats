package ru.akvine.fitstats.controllers.telegram.converters;

import com.google.common.base.Preconditions;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.akvine.fitstats.controllers.telegram.dto.notification.diet.AddDietNotificationRequest;
import ru.akvine.fitstats.controllers.telegram.dto.notification.diet.DeleteDietNotificationRequest;
import ru.akvine.fitstats.enums.telegram.DietNotificationSubscriptionType;
import ru.akvine.fitstats.services.dto.telegram.AddDietNotificationSubscription;
import ru.akvine.fitstats.services.dto.telegram.DeleteDietNotificationSubscription;
import ru.akvine.fitstats.services.dto.telegram.TelegramDietNotificationSubscription;

import java.util.List;

@Component
public class TelegramDietNotificationConverter {
    private final static String NEXT_LINE = "\n";

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

    public SendMessage convertToTelegramListDietNotificationRequest(String chatId, List<TelegramDietNotificationSubscription> notificationSubscriptions) {
        Preconditions.checkNotNull(notificationSubscriptions, "notificationSubscriptions is null");
        return new SendMessage(
                chatId,
                buildTelegramListDietNotificationRequest(notificationSubscriptions)
        );
    }

    private String buildTelegramListDietNotificationRequest(List<TelegramDietNotificationSubscription> notificationSubscriptions) {
        StringBuilder sb = new StringBuilder();
        sb.append("Список активных подписок: ").append(NEXT_LINE);

        notificationSubscriptions.forEach(subscription -> {
                    if (subscription.getDietNotificationSubscriptionType() == DietNotificationSubscriptionType.PROTEINS) {
                        sb.append("Превышение лимита по белкам").append(NEXT_LINE);
                    } else if (subscription.getDietNotificationSubscriptionType() == DietNotificationSubscriptionType.FATS) {
                        sb.append("Превышение лимита по жирам").append(NEXT_LINE);
                    } else if (subscription.getDietNotificationSubscriptionType() == DietNotificationSubscriptionType.CARBOHYDRATES) {
                        sb.append("Превышение лимита по углеводам").append(NEXT_LINE);
                    } else if (subscription.getDietNotificationSubscriptionType() == DietNotificationSubscriptionType.ENERGY) {
                        sb.append("Превышение лимита по энергии").append(NEXT_LINE);
                    } else {
                        sb.append("Превышение всех показателей: энергии, белков, жиров, углеводов").append(NEXT_LINE);
                    }
                }
        );

        return sb.toString();
    }
}
