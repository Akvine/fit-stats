package ru.akvine.fitstats.services.listeners;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.generics.TelegramBot;
import ru.akvine.fitstats.controllers.telegram.bot.TelegramLongPoolingBot;

@Component
@RequiredArgsConstructor
public class TelegramSendMessageListener {
    private final TelegramBot telegramBot;

    @EventListener
    @SneakyThrows
    public void sendMessageEvent(SendMessageEvent sendMessageEvent) {
        ((TelegramLongPoolingBot) telegramBot).sendMessage(sendMessageEvent.getSendMessage());
    }
}
