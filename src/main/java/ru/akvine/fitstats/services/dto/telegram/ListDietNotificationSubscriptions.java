package ru.akvine.fitstats.services.dto.telegram;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ListDietNotificationSubscriptions {
    private List<TelegramDietNotificationSubscription> subscriptions;
}
