package ru.akvine.fitstats.services.telegram.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.akvine.fitstats.exceptions.telegram.TelegramSubscriptionNotFoundException;
import ru.akvine.fitstats.services.dto.client.ClientBean;
import ru.akvine.fitstats.services.dto.telegram.TelegramSubscriptionBean;
import ru.akvine.fitstats.services.telegram.TelegramSubscriptionService;

@Slf4j
public abstract class AbstractMessageHandler {
    protected final static Long TELEGRAM_SUBSCRIPTION_TYPE_ID = 1L;

    @Autowired
    protected TelegramSubscriptionService telegramSubscriptionService;

    public abstract BotApiMethod<?> processUpdate(Update update);

    public ClientBean getAuthenticateUser(Update update) throws TelegramSubscriptionNotFoundException {
        Long telegramId = getTelegramId(update);

        TelegramSubscriptionBean telegramSubscription = telegramSubscriptionService.findByTelegramIdAndTypeId(
                telegramId,
                TELEGRAM_SUBSCRIPTION_TYPE_ID);
        return telegramSubscription.getClient();
    }

    protected abstract Long getTelegramId(Update update);
}
