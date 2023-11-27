package ru.akvine.fitstats.controllers.telegram.bot;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public class DummyTelegramBot extends TelegramWebhookBot {
    private static final String DUMMY_TOKEN = "dummy token";

    public DummyTelegramBot(DefaultBotOptions defaultBotOptions) {
        super(defaultBotOptions, DUMMY_TOKEN);
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return null;
    }

    @Override
    public String getBotPath() {
        return null;
    }

    @Override
    public String getBotUsername() {
        return null;
    }
}
