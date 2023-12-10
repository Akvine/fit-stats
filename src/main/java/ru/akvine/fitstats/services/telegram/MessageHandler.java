package ru.akvine.fitstats.services.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.akvine.fitstats.controllers.telegram.TelegramDietNotificationSubscriptionResolver;
import ru.akvine.fitstats.controllers.telegram.TelegramDietResolver;
import ru.akvine.fitstats.controllers.telegram.TelegramProductResolver;
import ru.akvine.fitstats.controllers.telegram.dto.common.TelegramBaseRequest;
import ru.akvine.fitstats.controllers.telegram.dto.diet.TelegramDietAddRecord;
import ru.akvine.fitstats.controllers.telegram.dto.diet.TelegramDietDisplay;
import ru.akvine.fitstats.controllers.telegram.dto.notification.diet.AddDietNotificationRequest;
import ru.akvine.fitstats.controllers.telegram.dto.notification.diet.DeleteDietNotificationRequest;
import ru.akvine.fitstats.controllers.telegram.dto.product.TelegramProductAddRequest;
import ru.akvine.fitstats.controllers.telegram.dto.product.TelegramProductListRequest;
import ru.akvine.fitstats.controllers.telegram.parser.TelegramProductParser;
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
    private final TelegramDietNotificationSubscriptionResolver telegramDietNotificationSubscriptionResolver;
    private final TelegramProductParser telegramProductParser;

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
        Long telegramId = message.getFrom().getId();

        try {
            if (waitingStates.containsKey(clientUuid)) {
                return processWaitingState(text, chatId, clientUuid, telegramId);
            }

            if (commandResolver.isStartCommand(text)) {
                String welcomeMessage = "Добро пожаловать! Вам доступен полный функционал бота Fit-Stats";
                return baseMessagesFactory.getMainMenuKeyboard(chatId, welcomeMessage);
            } else if (commandResolver.isHelpCommand(text)) {
                return baseMessagesFactory.getHelpMessage(chatId);
            } else if (commandResolver.isBackCommand(text)) {
                return baseMessagesFactory.getMainMenuKeyboard(chatId, "Выберите действие: ");
            } else if (commandResolver.isProductButton(text)) {
                return baseMessagesFactory.getProductKeyboard(chatId);
            } else if (commandResolver.isProductAddCommand(text)) {
                waitingStates.put(clientUuid, text);
                return baseMessagesFactory.getProductAddInputWaiting(chatId);
            } else if (commandResolver.isProductListCommand(text)) {
                waitingStates.put(clientUuid, text);
                return baseMessagesFactory.getProductListFilter(chatId);
            } else if (commandResolver.isDietButton(text)) {
                return baseMessagesFactory.getDietKeyboard(chatId);
            } else if (commandResolver.isDietStatisticDisplayCommand(text)) {
                TelegramDietDisplay telegramDietDisplay = new TelegramDietDisplay(clientUuid, chatId);
                return telegramDietResolver.display(telegramDietDisplay);
            } else if (commandResolver.isDietAddRecordCommand(text)) {
                waitingStates.put(clientUuid, text);
                return baseMessagesFactory.getDietRecordAddInputWaiting(chatId);
            } else if (commandResolver.isDietListRecordCommand(text)) {
                TelegramBaseRequest telegramBaseRequest = new TelegramBaseRequest(clientUuid, chatId, telegramId);
                return telegramDietResolver.listRecord(telegramBaseRequest);
            } else if (commandResolver.isNotificationSubscriptionButton(text)) {
                return baseMessagesFactory.getNotificationSubscriptionTypesKeyboard(chatId);
            } else if (commandResolver.isNotificationSubscriptionDietButton(text)) {
                return baseMessagesFactory.getDietNotificationSubscriptionKeyboard(chatId);
            } else if (commandResolver.isNotificationSubscriptionDietAdd(text)) {
                waitingStates.put(clientUuid, text);
                return baseMessagesFactory.getNotificationSubscriptionDietAdd(chatId);
            } else if (commandResolver.isNotificationSubscriptionDietList(text)) {
                TelegramBaseRequest telegramBaseRequest = new TelegramBaseRequest(clientUuid, chatId, telegramId);
                return telegramDietNotificationSubscriptionResolver.list(telegramBaseRequest);
            } else if (commandResolver.isNotificationSubscriptionDietDelete(text)) {
                waitingStates.put(clientUuid, text);
                return baseMessagesFactory.getNotificationSubscriptionDietDelete(chatId);
            } else {
                return baseMessagesFactory.getInvalidMessage(chatId);
            }
        } catch (Exception exception) {
            return telegramExceptionHandler.handle(chatId, exception);
        }
    }

    private BotApiMethod<?> processWaitingState(String text, String chatId, String clientUuid, Long telegramId) {
        if (commandResolver.isProductListCommand(waitingStates.get(clientUuid))) {
            waitingStates.remove(clientUuid);
            TelegramProductListRequest telegramProductListRequest = new TelegramProductListRequest(clientUuid, chatId, text);
            return telegramProductResolver.list(telegramProductListRequest);
        } else if (commandResolver.isProductAddCommand(waitingStates.get(clientUuid))) {
            waitingStates.remove(clientUuid);
            TelegramProductAddRequest telegramProductAddRequest = telegramProductParser.parseTelegramProductAddRequest(text);
            telegramProductAddRequest
                    .setChatId(chatId)
                    .setClientUuid(clientUuid)
                    .setTelegramId(telegramId);
            return telegramProductResolver.add(telegramProductAddRequest);
        } else if (commandResolver.isDietAddRecordCommand(waitingStates.get(clientUuid))) {
            waitingStates.remove(clientUuid);
            TelegramDietAddRecord telegramDietAddRecord = new TelegramDietAddRecord(clientUuid, chatId, text);
            return telegramDietResolver.addRecord(telegramDietAddRecord);
        } else if (commandResolver.isNotificationSubscriptionDietAdd(waitingStates.get(clientUuid))) {
            waitingStates.remove(clientUuid);
            AddDietNotificationRequest request = new AddDietNotificationRequest(clientUuid, chatId, telegramId, text);
            return telegramDietNotificationSubscriptionResolver.add(request);
        } else if (commandResolver.isNotificationSubscriptionDietDelete(waitingStates.get(clientUuid))) {
            waitingStates.remove(clientUuid);
            DeleteDietNotificationRequest request = new DeleteDietNotificationRequest(clientUuid, chatId, telegramId, text);
            return telegramDietNotificationSubscriptionResolver.delete(request);
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
        telegramAuthService.authenticateTelegramClient(text, telegramId, chatId);
        String welcomeMessage = "Добро пожаловать! Вам доступен полный функционал бота Fit-Stats";
        return baseMessagesFactory.getMainMenuKeyboard(chatId, welcomeMessage);
    }

    private String getChatId(Message message) {
        return String.valueOf(message.getChatId());
    }
}
