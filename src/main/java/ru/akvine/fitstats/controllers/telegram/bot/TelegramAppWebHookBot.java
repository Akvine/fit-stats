package ru.akvine.fitstats.controllers.telegram.bot;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.akvine.fitstats.services.telegram.handler.MessageHandler;
import ru.akvine.fitstats.services.telegram.handler.MessageHandlerFacade;

@RequiredArgsConstructor
public class TelegramAppWebHookBot extends TelegramWebhookBot {
    private String botPath;
    private String botUsername;

    private final MessageHandlerFacade messageHandlerFacade;

    public TelegramAppWebHookBot(DefaultBotOptions defaultBotOptions,
                                 String botToken,
                                 MessageHandlerFacade messageHandlerFacade) {
        super(defaultBotOptions, botToken);
        this.messageHandlerFacade = messageHandlerFacade;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return messageHandlerFacade.processUpdate(update);
    }

    @Override
    public String getBotPath() {
        return botPath;
    }

    public void setBotUsername(String botUsername) {
        this.botUsername = botUsername;
    }

    public void setBotPath(String botPath) {
        this.botPath = botPath;
    }
}
