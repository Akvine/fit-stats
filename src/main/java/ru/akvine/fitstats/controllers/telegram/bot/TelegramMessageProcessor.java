package ru.akvine.fitstats.controllers.telegram.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.akvine.fitstats.services.properties.PropertyParseService;

import java.io.Serializable;

@Component
@Slf4j
@RequiredArgsConstructor
public class TelegramMessageProcessor {
    @Value("telegram.bot.enabled")
    private String enabledPropertyName;

    private final PropertyParseService propertyParseService;
    private final TelegramWebhookBot telegramWebhookBot;

    public BotApiMethod<? extends Serializable> processIncomingMessage(Update update) {
        boolean isEnabled = propertyParseService.parseBoolean(enabledPropertyName);
        if (!isEnabled) {
            logger.debug("Telegram bot disabled");
            return null;
        }
        return telegramWebhookBot.onWebhookUpdateReceived(update);
    }
}
