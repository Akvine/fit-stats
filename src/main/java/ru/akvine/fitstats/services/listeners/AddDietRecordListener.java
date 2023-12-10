package ru.akvine.fitstats.services.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.services.telegram.TelegramDietNotificationSubscriptionService;

@Component
@RequiredArgsConstructor
public class AddDietRecordListener {
    private final TelegramDietNotificationSubscriptionService telegramDietNotificationSubscriptionService;

    @EventListener
    public void acceptEvent(AddDietRecordEvent event) {
        telegramDietNotificationSubscriptionService.notifyIfNeed(event.getClientId(), event.getDietSettingEntity(), event.getRecords());
    }
}
