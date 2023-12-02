package ru.akvine.fitstats.controllers.telegram.bot;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.akvine.fitstats.services.telegram.MessageHandler;

public class TelegramLongPoolingBot extends TelegramLongPollingBot {
    private String botUsername;

    private final MessageHandler messageHandler;

    public TelegramLongPoolingBot(DefaultBotOptions options, String botToken, String botUsername, MessageHandler messageHandler) {
        super(options, botToken);
        this.botUsername = botUsername;
        this.messageHandler = messageHandler;
    }

    @Override
    public void onUpdateReceived(Update update) {
        sendMessage(messageHandler.processUpdate(update));
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    private void sendMessage(BotApiMethod<?> message) {
        try {
            execute(message);
        } catch (Exception exception) {
            throw new RuntimeException("Some error has occurred, ex=" + exception.getMessage());
        }
    }
}
