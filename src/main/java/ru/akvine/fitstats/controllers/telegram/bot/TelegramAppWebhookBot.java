package ru.akvine.fitstats.controllers.telegram.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.akvine.fitstats.services.properties.PropertyParseService;
import ru.akvine.fitstats.services.telegram.MessageHandler;

@RequiredArgsConstructor
public class TelegramAppWebhookBot extends TelegramWebhookBot {
    @Value("telegram.bot.usernam}")
    private String botUsername;
    @Value("telegram.bot.webhook-path")
    private String botWebhookPath;

    private final MessageHandler messageHandler;
    private final PropertyParseService propertyParseService;

    public TelegramAppWebhookBot(DefaultBotOptions defaultBotOptions,
                                 MessageHandler messageHandler,
                                 String botToken,
                                 PropertyParseService propertyParseService) {
        super(defaultBotOptions, botToken);
        this.propertyParseService = propertyParseService;
        this.messageHandler = messageHandler;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return messageHandler.processUpdate(update);
    }

    @Override
    public String getBotPath() {
        return propertyParseService.get(botWebhookPath);
    }

    @Override
    public String getBotUsername() {
        return propertyParseService.get(botUsername);
    }
}
