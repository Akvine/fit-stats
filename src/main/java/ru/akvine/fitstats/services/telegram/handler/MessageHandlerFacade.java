package ru.akvine.fitstats.services.telegram.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.akvine.fitstats.exceptions.telegram.TelegramConfigurationException;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessageHandlerFacade {

    private final MessageHandler messageHandler;

    public BotApiMethod<?> processUpdate(Update update) {
        logger.debug("Incoming update=[{}]", update);

        if (update.hasCallbackQuery()) {
            throw new RuntimeException("Not supporting callback!");
        } else if (update.hasMessage()) {
            return messageHandler.processUpdate(update);
        }

        logger.debug("Not supported Update=[{}]", update);
        throw new TelegramConfigurationException("Update not supported. Check allowed_updates");
    }
}
