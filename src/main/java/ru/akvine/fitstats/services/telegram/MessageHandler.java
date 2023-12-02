package ru.akvine.fitstats.services.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.akvine.fitstats.controllers.telegram.TelegramDietResolver;
import ru.akvine.fitstats.controllers.telegram.TelegramProductResolver;
import ru.akvine.fitstats.controllers.telegram.dto.diet.TelegramDietAddRecord;
import ru.akvine.fitstats.controllers.telegram.dto.diet.TelegramDietDisplay;
import ru.akvine.fitstats.controllers.telegram.dto.product.TelegramProductList;
import ru.akvine.fitstats.exceptions.telegram.TelegramAuthCodeNotFoundException;
import ru.akvine.fitstats.exceptions.telegram.TelegramSubscriptionNotFoundException;
import ru.akvine.fitstats.services.dto.client.ClientBean;
import ru.akvine.fitstats.services.telegram.factory.BaseMessagesFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class MessageHandler {
    private final TelegramExceptionHandler telegramExceptionHandler;
    private final BaseMessagesFactory baseMessagesFactory;
    private final CommandResolver commandResolver;
    private final TelegramAuthService telegramAuthService;
    private final TelegramDietResolver telegramDietResolver;
    private final TelegramProductResolver telegramProductResolver;

    private final Map<String, String> waitingStates = new ConcurrentHashMap<>();

    public BotApiMethod<?> processUpdate(Update update) {
        Message message = update.getMessage();

        try {
            ClientBean client = telegramAuthService.getAuthenticateUser(update);
            return processAuthenticatedUserUpdate(message, client);
        } catch (TelegramSubscriptionNotFoundException exception) {
            return processNotAuthenticatedUserUpdate(message);
        }
    }

    private BotApiMethod<?> processAuthenticatedUserUpdate(Message message, ClientBean client) {
        String chatId = getChatId(message);
        String text = message.getText();
        String clientUuid = client.getUuid();

        try {
            if (waitingStates.containsKey(clientUuid)) {
                return processWaitingState(text, chatId, clientUuid);
            }

            if (commandResolver.isStartCommand(text)) {
                return baseMessagesFactory.getMainMenuKeyboard(chatId);
            } else if (commandResolver.isProductListCommand(text)) {
                waitingStates.put(clientUuid, text);
                return baseMessagesFactory.getProductListFilter(chatId);
            } else if (commandResolver.isDietStatisticDisplayCommand(text)) {
                TelegramDietDisplay telegramDietDisplay = new TelegramDietDisplay(clientUuid, chatId);
                return telegramDietResolver.display(telegramDietDisplay);
            } else if (commandResolver.isDietAddRecordCommand(text)) {
                waitingStates.put(clientUuid, text);
                return baseMessagesFactory.getDietRecordAddInputWaiting(chatId);
            } else if (commandResolver.isHelpCommand(text)) {
                return baseMessagesFactory.getHelpMessage(chatId);
            } else {
                return baseMessagesFactory.getInvalidMessage(chatId);
            }
        } catch (Exception exception) {
            return telegramExceptionHandler.handle(chatId, exception);
        }
    }

    private BotApiMethod<?> processWaitingState(String text, String chatId, String clientUuid) {
        if (commandResolver.isProductListCommand(waitingStates.get(clientUuid))) {
            waitingStates.remove(clientUuid);
            TelegramProductList telegramProductList = new TelegramProductList(clientUuid, chatId, text);
            return telegramProductResolver.list(telegramProductList);
        } else if (commandResolver.isDietAddRecordCommand(waitingStates.get(clientUuid))) {
            waitingStates.remove(clientUuid);
            TelegramDietAddRecord telegramDietAddRecord = new TelegramDietAddRecord(clientUuid, chatId, text);
            return telegramDietResolver.addRecord(telegramDietAddRecord);
        }
        throw new IllegalStateException("Unknown command = [" + waitingStates.remove(clientUuid) + "]");
    }

    private BotApiMethod<?> processNotAuthenticatedUserUpdate(Message message) {
        String chatId = getChatId(message);
        Long telegramId = message.getFrom().getId();
        String text = message.getText();

        try {
            return authenticateTelegramClient(text, chatId, telegramId);
        } catch (TelegramAuthCodeNotFoundException exception) {
            return baseMessagesFactory.getInvalidAuthCode(chatId);
        }
    }

    private BotApiMethod<?> authenticateTelegramClient(String text, String chatId, Long telegramId) {
        telegramAuthService.authenticateTelegramClient(text, telegramId);
        return baseMessagesFactory.getMainMenuKeyboard(chatId);
    }

    private String getChatId(Message message) {
        return String.valueOf(message.getChatId());
    }
}
