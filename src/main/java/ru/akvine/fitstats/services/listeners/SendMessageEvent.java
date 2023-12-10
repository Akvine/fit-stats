package ru.akvine.fitstats.services.listeners;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Getter
public class SendMessageEvent extends ApplicationEvent {

    private final SendMessage sendMessage;

    public SendMessageEvent(Object source, SendMessage sendMessage) {
        super(source);
        this.sendMessage = sendMessage;
    }
}
