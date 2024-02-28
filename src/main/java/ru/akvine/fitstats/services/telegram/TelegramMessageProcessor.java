package ru.akvine.fitstats.services.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.akvine.fitstats.services.properties.PropertyParseService;

import java.io.Serializable;

@Service
@RequiredArgsConstructor
public class TelegramMessageProcessor implements MessageProcessor {
    @Value("telegram.bot.enabled")
    private String enabled;

    private final TelegramLongPollingBot bot;
    private final PropertyParseService propertyParseService;

    @Override
    public BotApiMethod<? extends Serializable> processIncomingMessage(Update update) {
        boolean isEnabled = propertyParseService.parseBoolean(enabled);
        if (!isEnabled) {
            return null;
        }
        bot.onUpdateReceived(update);
        return null;
    }
}
