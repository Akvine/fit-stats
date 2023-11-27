package ru.akvine.fitstats.controllers.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.akvine.fitstats.services.telegram.MessageProcessor;

@RestController
@RequiredArgsConstructor
public class TelegramWebHookController implements TelegramWebHookControllerMeta {
    private final MessageProcessor messageProcessor;

    @Override
    public BotApiMethod<?> onUpdateReceived(Update update) {
        return messageProcessor.processIncomingMessage(update);
    }
}
