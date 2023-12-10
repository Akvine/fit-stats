package ru.akvine.fitstats.services.dto.telegram;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.enums.telegram.DietNotificationSubscriptionType;

@Data
@Accessors(chain = true)
public class AddDietNotificationSubscription {
    private Long telegramId;
    private String clientUuid;
    private DietNotificationSubscriptionType type;
}
