package ru.akvine.fitstats.controllers.telegram;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequestMapping(value = "/telegram/bot")
public interface TelegramWebHookControllerMeta {
    @PostMapping(value = "/{botSecret}")
    BotApiMethod<?> onUpdateReceived(@PathVariable("botSecret") String botSecret, @RequestBody Update update);
}
