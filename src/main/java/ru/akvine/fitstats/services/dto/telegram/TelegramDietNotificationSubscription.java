package ru.akvine.fitstats.services.dto.telegram;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.entities.telegram.TelegramDietNotificationSubscriptionEntity;
import ru.akvine.fitstats.enums.telegram.DietNotificationSubscriptionType;
import ru.akvine.fitstats.services.dto.client.ClientBean;

@Data
@Accessors(chain = true)
public class TelegramDietNotificationSubscription {
    private Long id;
    private Long telegramId;
    private ClientBean client;
    private boolean processed;
    private DietNotificationSubscriptionType dietNotificationSubscriptionType;

    public TelegramDietNotificationSubscription(TelegramDietNotificationSubscriptionEntity telegramDietNotificationSubscriptionEntity) {
        this.id = telegramDietNotificationSubscriptionEntity.getId();
        this.telegramId = telegramDietNotificationSubscriptionEntity.getTelegramId();
        this.client = new ClientBean(telegramDietNotificationSubscriptionEntity.getClient());
        this.processed = telegramDietNotificationSubscriptionEntity.isProcessed();
        this.dietNotificationSubscriptionType = telegramDietNotificationSubscriptionEntity.getDietNotificationSubscriptionType();
    }
}
