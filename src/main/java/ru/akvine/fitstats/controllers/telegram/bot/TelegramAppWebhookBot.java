package ru.akvine.fitstats.controllers.telegram.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.akvine.fitstats.services.telegram.MessageHandler;

@RequiredArgsConstructor
public class TelegramAppWebhookBot extends TelegramWebhookBot {
    @Value("${telegram.bot.username}")
    private String botUsername;
    @Value("${telegram.bot.webhook-path}")
    private String botWebhookPath;

    private final MessageHandler messageHandler;

    public TelegramAppWebhookBot(DefaultBotOptions defaultBotOptions, MessageHandler messageHandler, String botToken) {
        super(defaultBotOptions, botToken);
        this.messageHandler = messageHandler;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return messageHandler.processUpdate(update);
    }

    @Override
    public String getBotPath() {
        return botWebhookPath;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }
}
