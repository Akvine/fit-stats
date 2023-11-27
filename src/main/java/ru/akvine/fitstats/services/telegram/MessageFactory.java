package ru.akvine.fitstats.services.telegram;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class MessageFactory {
    public SendMessage getDummyMessage(String chatId) {
        return new SendMessage(chatId, "Приветственное сообщение!");
    }
}
