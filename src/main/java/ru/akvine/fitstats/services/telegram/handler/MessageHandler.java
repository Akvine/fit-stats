package ru.akvine.fitstats.services.telegram.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.akvine.fitstats.controllers.telegram.*;
import ru.akvine.fitstats.controllers.telegram.dto.common.TelegramBaseRequest;
import ru.akvine.fitstats.controllers.telegram.dto.diet.TelegramDietAddRecordRequest;
import ru.akvine.fitstats.controllers.telegram.dto.diet.TelegramDietDeleteRecordRequest;
import ru.akvine.fitstats.controllers.telegram.dto.diet.TelegramDietDisplayRequest;
import ru.akvine.fitstats.controllers.telegram.dto.notification.diet.AddDietNotificationRequest;
import ru.akvine.fitstats.controllers.telegram.dto.notification.diet.DeleteDietNotificationRequest;
import ru.akvine.fitstats.controllers.telegram.dto.product.TelegramProductAddRequest;
import ru.akvine.fitstats.controllers.telegram.dto.product.TelegramProductListRequest;
import ru.akvine.fitstats.controllers.telegram.dto.profile.TelegramProfileUpdateBiometricRequest;
import ru.akvine.fitstats.controllers.telegram.dto.statistic.TelegramStatisticHistoryRequest;
import ru.akvine.fitstats.controllers.telegram.parser.TelegramProductParser;
import ru.akvine.fitstats.controllers.telegram.parser.TelegramProfileParser;
import ru.akvine.fitstats.controllers.telegram.parser.TelegramStatisticParser;
import ru.akvine.fitstats.exceptions.telegram.TelegramAuthCodeNotFoundException;
import ru.akvine.fitstats.exceptions.telegram.TelegramSubscriptionNotFoundException;
import ru.akvine.fitstats.services.dto.client.ClientBean;
import ru.akvine.fitstats.services.telegram.CommandResolver;
import ru.akvine.fitstats.services.telegram.TelegramAuthService;
import ru.akvine.fitstats.services.telegram.TelegramExceptionHandler;
import ru.akvine.fitstats.services.telegram.factory.BaseMessagesFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class MessageHandler extends AbstractMessageHandler {
    private final TelegramExceptionHandler telegramExceptionHandler;
    private final BaseMessagesFactory baseMessagesFactory;
    private final CommandResolver commandResolver;
    private final TelegramAuthService telegramAuthService;

    private final TelegramDietResolver telegramDietResolver;
    private final TelegramProductResolver telegramProductResolver;
    private final TelegramDietNotificationSubscriptionResolver telegramDietNotificationSubscriptionResolver;
    private final TelegramProfileResolver telegramProfileResolver;
    private final TelegramStatisticResolver telegramStatisticResolver;

    private final TelegramProductParser telegramProductParser;
    private final TelegramProfileParser telegramProfileParser;
    private final TelegramStatisticParser telegramStatisticParser;

    private final Map<String, String> waitingStates = new ConcurrentHashMap<>();

    @Override
    public BotApiMethod<?> processUpdate(Update update) {
        Message message = update.getMessage();

        try {
            ClientBean client = telegramAuthService.getAuthenticateUser(update);
            return processAuthenticatedUserUpdate(message, client);
        } catch (TelegramSubscriptionNotFoundException exception) {
            return processNotAuthenticatedUserUpdate(message);
        }
    }

    @Override
    protected Long getTelegramId(Update update) {
        return update.getMessage().getFrom().getId();
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
                TelegramDietDisplayRequest telegramDietDisplayRequest = new TelegramDietDisplayRequest(clientUuid, chatId);
                return telegramDietResolver.display(telegramDietDisplayRequest);
            } else if (commandResolver.isDietAddRecordCommand(text)) {
                waitingStates.put(clientUuid, text);
                return baseMessagesFactory.getDietRecordAddInputWaiting(chatId);
            } else if (commandResolver.isDietListRecordCommand(text)) {
                TelegramBaseRequest request = new TelegramBaseRequest(clientUuid, chatId, telegramId);
                return telegramDietResolver.listRecord(request);
            } else if (commandResolver.isDietDeleteRecordCommand(text)) {
                waitingStates.put(clientUuid, text);
                return baseMessagesFactory.getDietRecordDeleteInputWaiting(chatId);
            } else if (commandResolver.isProfileButton(text)) {
                return baseMessagesFactory.getProfileKeyboard(chatId);
            } else if (commandResolver.isProfileBiometricDisplayCommand(text)) {
                TelegramBaseRequest request = new TelegramBaseRequest(clientUuid, chatId, telegramId);
                return telegramProfileResolver.biometricDisplay(request);
            } else if (commandResolver.isProfileBiometricUpdateCommand(text)) {
                waitingStates.put(clientUuid, text);
                return baseMessagesFactory.getProfileUpdateBiometricInputWaiting(chatId);
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
            } else if (commandResolver.isStatisticButton(text)) {
                return baseMessagesFactory.getStatisticKeyboard(chatId);
            } else if (commandResolver.isStatisticHistoryCommand(text)) {
                waitingStates.put(clientUuid, text);
                return baseMessagesFactory.getStatisticHistoryInputWaiting(chatId);
            } else if (commandResolver.isStatisticIndicatorsCommand(text)) {
                TelegramBaseRequest request = new TelegramBaseRequest(clientUuid, chatId, telegramId);
                return telegramStatisticResolver.indicators(request);
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
            TelegramProductAddRequest request = (TelegramProductAddRequest) telegramProductParser.parseTelegramProductAddRequest(text)
                    .setChatId(chatId)
                    .setClientUuid(clientUuid)
                    .setTelegramId(telegramId);
            return telegramProductResolver.add(request);
        } else if (commandResolver.isDietAddRecordCommand(waitingStates.get(clientUuid))) {
            waitingStates.remove(clientUuid);
            TelegramDietAddRecordRequest request = new TelegramDietAddRecordRequest(clientUuid, chatId, text);
            return telegramDietResolver.addRecord(request);
        } else if (commandResolver.isDietDeleteRecordCommand(waitingStates.get(clientUuid))) {
            waitingStates.remove(clientUuid);
            TelegramDietDeleteRecordRequest request = new TelegramDietDeleteRecordRequest(text, clientUuid, chatId);
            return telegramDietResolver.deleteRecord(request);
        } else if (commandResolver.isProfileBiometricUpdateCommand(waitingStates.get(clientUuid))) {
            waitingStates.remove(clientUuid);
            TelegramProfileUpdateBiometricRequest request = telegramProfileParser.parseToTelegramProfileUpdateBiometricRequest(chatId, clientUuid, text);
            return telegramProfileResolver.biometricUpdate(request);
        } else if (commandResolver.isNotificationSubscriptionDietAdd(waitingStates.get(clientUuid))) {
            waitingStates.remove(clientUuid);
            AddDietNotificationRequest request = new AddDietNotificationRequest(clientUuid, chatId, telegramId, text);
            return telegramDietNotificationSubscriptionResolver.add(request);
        } else if (commandResolver.isNotificationSubscriptionDietDelete(waitingStates.get(clientUuid))) {
            waitingStates.remove(clientUuid);
            DeleteDietNotificationRequest request = new DeleteDietNotificationRequest(clientUuid, chatId, telegramId, text);
            return telegramDietNotificationSubscriptionResolver.delete(request);
        } else if (commandResolver.isStatisticHistoryCommand(waitingStates.get(clientUuid))) {
            waitingStates.remove(clientUuid);
            TelegramStatisticHistoryRequest request = telegramStatisticParser.parseToTelegramStatisticHistoryRequest(clientUuid, chatId, text);
            return telegramStatisticResolver.history(request);
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
