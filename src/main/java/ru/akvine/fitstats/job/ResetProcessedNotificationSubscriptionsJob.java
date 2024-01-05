package ru.akvine.fitstats.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.repositories.telegram.TelegramDietNotificationSubscriptionRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class ResetProcessedNotificationSubscriptionsJob {
    private final TelegramDietNotificationSubscriptionRepository telegramDietNotificationSubscriptionRepository;

    @Scheduled(cron = "${reset.diet.notification.subscriptions.cron}")
    public void resetProcessed() {
        logger.debug("Reset processed diet notification subscriptions");
        telegramDietNotificationSubscriptionRepository.resetProcessedSubscriptions();
        logger.debug("Success reset processed diet notification subscriptions");
    }
}
