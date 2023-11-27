package ru.akvine.fitstats.services.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class MessageHandler {
    private final MessageFactory messageFactory;

    public BotApiMethod<?> processUpdate(Update update) {
        String chatId = getChatId(update.getMessage());
        return messageFactory.getDummyMessage(chatId);
    }

    private String getChatId(Message message) {
        return String.valueOf(message.getChatId());
    }
}
