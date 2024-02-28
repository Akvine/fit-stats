package ru.akvine.fitstats.services.telegram;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;

public interface MessageProcessor {
    BotApiMethod<? extends Serializable> processIncomingMessage(Update update);
}
