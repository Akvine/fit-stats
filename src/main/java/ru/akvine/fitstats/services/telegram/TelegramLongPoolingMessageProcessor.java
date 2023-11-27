package ru.akvine.fitstats.services.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;

@Service
@RequiredArgsConstructor
public class TelegramLongPoolingMessageProcessor implements MessageProcessor {
    @Value("${telegram.bot.enabled}")
    private boolean isEnabled;

    private final TelegramLongPollingBot bot;

    @Override
    public BotApiMethod<? extends Serializable> processIncomingMessage(Update update) {
        if (!isEnabled) {
            return null;
        }
        bot.onUpdateReceived(update);
        return null;
    }
}
