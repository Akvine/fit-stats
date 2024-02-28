package ru.akvine.fitstats.controllers.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.akvine.fitstats.controllers.telegram.validators.TelegramBotSecretValidator;

//@RestController
//@RequiredArgsConstructor
//public class TelegramWebHookController implements TelegramWebHookControllerMeta {
//    private final TelegramMessageProcessor messageProcessor;
//    private final TelegramBotSecretValidator telegramBotSecretValidator;
//
//    @Override
//    public BotApiMethod<?> onUpdateReceived(String botSecret, Update update) {
//        telegramBotSecretValidator.verifyBotPathSecret(botSecret);
//        return messageProcessor.processIncomingMessage(update);
//    }
//}
