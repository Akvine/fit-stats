package ru.akvine.fitstats.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import ru.akvine.fitstats.services.properties.ExternalAppPropertyService;

@RequiredArgsConstructor
@Slf4j
public class RefreshPropertiesJob {
    private final ExternalAppPropertyService externalAppPropertyService;

    @Scheduled(cron = "${reset.diet.notification.subscriptions.cron}")
    public void schedule() {
        logger.debug("Refresh properties by scheduler starts...");
        externalAppPropertyService.refresh();
        logger.debug("Refresh properties by scheduler end");
    }
}
