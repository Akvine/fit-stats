package ru.akvine.fitstats.services.telegram.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.akvine.fitstats.constants.MessageResolverCodes;
import ru.akvine.fitstats.context.ClientSettingsContext;
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
import ru.akvine.fitstats.controllers.telegram.dto.profile.TelegramProfileUpdateSettingsRequest;
import ru.akvine.fitstats.controllers.telegram.dto.statistic.TelegramStatisticHistoryRequest;
import ru.akvine.fitstats.controllers.telegram.parser.TelegramProductParser;
import ru.akvine.fitstats.controllers.telegram.parser.TelegramProfileParser;
import ru.akvine.fitstats.controllers.telegram.parser.TelegramStatisticParser;
import ru.akvine.fitstats.enums.Language;
import ru.akvine.fitstats.exceptions.security.BlockedCredentialsException;
import ru.akvine.fitstats.exceptions.telegram.TelegramAuthCodeNotFoundException;
import ru.akvine.fitstats.exceptions.telegram.TelegramSubscriptionNotFoundException;
import ru.akvine.fitstats.services.MessageResolveService;
import ru.akvine.fitstats.services.dto.client.ClientBean;
import ru.akvine.fitstats.services.security.BlockingService;
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
    private final BlockingService blockingService;
    private final TelegramAuthService telegramAuthService;
    private final MessageResolveService messageResolveService;

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
        String email = client.getEmail();
        Long telegramId = message.getFrom().getId();
        Language language = ClientSettingsContext.getClientSettingsContextHolder()
                .getByEmail(email)
                .getLanguage();

        try {
            checkIsBlockedAndThrow(email);

            if (waitingStates.containsKey(clientUuid)) {
                return processWaitingState(text, chatId, email, clientUuid, telegramId, language);
            }

            if (commandResolver.isStartCommand(text, language)) {
                String welcomeMessage = messageResolveService.message(MessageResolverCodes.TELEGRAM_WELCOME_MESSAGE_CODE, language);
                return baseMessagesFactory.getMainMenuKeyboard(chatId, welcomeMessage, language);
            } else if (commandResolver.isHelpCommand(text, language)) {
                return baseMessagesFactory.getHelpMessage(chatId, language);
            } else if (commandResolver.isBackCommand(text, language)) {
                String chooseActionMessage = messageResolveService.message(MessageResolverCodes.TELEGRAM_CHOOSE_ACTION_CODE, language);
                return baseMessagesFactory.getMainMenuKeyboard(chatId, chooseActionMessage + ": ", language);
            } else if (commandResolver.isProductButton(text, language)) {
                return baseMessagesFactory.getProductKeyboard(chatId, language);
            } else if (commandResolver.isProductAddCommand(text, language)) {
                waitingStates.put(clientUuid, text);
                return baseMessagesFactory.getProductAddInputWaiting(chatId, language);
            } else if (commandResolver.isProductListCommand(text, language)) {
                waitingStates.put(clientUuid, text);
                return baseMessagesFactory.getProductListFilter(chatId, language);
            } else if (commandResolver.isDietButton(text, language)) {
                return baseMessagesFactory.getDietKeyboard(chatId, language);
            } else if (commandResolver.isDietStatisticDisplayCommand(text, language)) {
                TelegramDietDisplayRequest telegramDietDisplayRequest = new TelegramDietDisplayRequest(clientUuid, chatId);
                return telegramDietResolver.display(telegramDietDisplayRequest);
            } else if (commandResolver.isDietAddRecordCommand(text, language)) {
                waitingStates.put(clientUuid, text);
                return baseMessagesFactory.getDietRecordAddInputWaiting(chatId, language);
            } else if (commandResolver.isDietListRecordCommand(text, language)) {
                TelegramBaseRequest request = new TelegramBaseRequest(clientUuid, email, chatId, telegramId);
                return telegramDietResolver.listRecord(request);
            } else if (commandResolver.isDietDeleteRecordCommand(text, language)) {
                waitingStates.put(clientUuid, text);
                return baseMessagesFactory.getDietRecordDeleteInputWaiting(chatId, language);
            } else if (commandResolver.isProfileButton(text, language)) {
                return baseMessagesFactory.getProfileKeyboard(chatId, language);
            } else if (commandResolver.isProfileBiometricDisplayCommand(text, language)) {
                TelegramBaseRequest request = new TelegramBaseRequest(clientUuid, email, chatId, telegramId);
                return telegramProfileResolver.biometricDisplay(request);
            } else if (commandResolver.isProfileBiometricUpdateCommand(text, language)) {
                waitingStates.put(clientUuid, text);
                return baseMessagesFactory.getProfileUpdateBiometricInputWaiting(chatId, language);
            } else if (commandResolver.isProfileSettingsCommand(text, language)) {
                TelegramBaseRequest request = new TelegramBaseRequest(clientUuid, email, chatId, telegramId);
                return telegramProfileResolver.settingsList(request);
            } else if (commandResolver.isProfileSettingsUpdateCommand(text, language)) {
                waitingStates.put(clientUuid, text);
                return baseMessagesFactory.getProfileSettingsUpdateInputWaiting(chatId, language);
            } else if (commandResolver.isNotificationSubscriptionButton(text, language)) {
                return baseMessagesFactory.getNotificationSubscriptionTypesKeyboard(chatId, language);
            } else if (commandResolver.isNotificationSubscriptionDietButton(text, language)) {
                return baseMessagesFactory.getDietNotificationSubscriptionKeyboard(chatId, language);
            } else if (commandResolver.isNotificationSubscriptionDietAdd(text, language)) {
                waitingStates.put(clientUuid, text);
                return baseMessagesFactory.getNotificationSubscriptionDietAdd(chatId, language);
            } else if (commandResolver.isNotificationSubscriptionDietList(text, language)) {
                TelegramBaseRequest telegramBaseRequest = new TelegramBaseRequest(clientUuid, email, chatId, telegramId);
                return telegramDietNotificationSubscriptionResolver.list(telegramBaseRequest);
            } else if (commandResolver.isStatisticButton(text, language)) {
                return baseMessagesFactory.getStatisticKeyboard(chatId, language);
            } else if (commandResolver.isStatisticHistoryCommand(text, language)) {
                waitingStates.put(clientUuid, text);
                return baseMessagesFactory.getStatisticHistoryInputWaiting(chatId, language);
            } else if (commandResolver.isStatisticIndicatorsCommand(text, language)) {
                TelegramBaseRequest request = new TelegramBaseRequest(clientUuid, email, chatId, telegramId);
                return telegramStatisticResolver.indicators(request);
            } else if (commandResolver.isNotificationSubscriptionDietDelete(text, language)) {
                waitingStates.put(clientUuid, text);
                return baseMessagesFactory.getNotificationSubscriptionDietDelete(chatId, language);
            } else {
                return baseMessagesFactory.getInvalidMessage(chatId, language);
            }
        } catch (Exception exception) {
            return telegramExceptionHandler.handle(chatId, exception);
        }
    }

    private BotApiMethod<?> processWaitingState(String text,
                                                String chatId,
                                                String email,
                                                String clientUuid,
                                                Long telegramId,
                                                Language language) {
        if (commandResolver.isProductListCommand(waitingStates.get(clientUuid), language)) {
            waitingStates.remove(clientUuid);
            TelegramProductListRequest telegramProductListRequest = new TelegramProductListRequest(clientUuid, chatId, text);
            return telegramProductResolver.list(telegramProductListRequest);
        } else if (commandResolver.isProductAddCommand(waitingStates.get(clientUuid), language)) {
            waitingStates.remove(clientUuid);
            TelegramProductAddRequest request = (TelegramProductAddRequest) telegramProductParser.parseTelegramProductAddRequest(text)
                    .setChatId(chatId)
                    .setClientUuid(clientUuid)
                    .setTelegramId(telegramId);
            return telegramProductResolver.add(request);
        } else if (commandResolver.isDietAddRecordCommand(waitingStates.get(clientUuid), language)) {
            waitingStates.remove(clientUuid);
            TelegramDietAddRecordRequest request = new TelegramDietAddRecordRequest(clientUuid, chatId, text);
            return telegramDietResolver.addRecord(request);
        } else if (commandResolver.isDietDeleteRecordCommand(waitingStates.get(clientUuid), language)) {
            waitingStates.remove(clientUuid);
            TelegramDietDeleteRecordRequest request = new TelegramDietDeleteRecordRequest(text, clientUuid, chatId);
            return telegramDietResolver.deleteRecord(request);
        } else if (commandResolver.isProfileBiometricUpdateCommand(waitingStates.get(clientUuid), language)) {
            waitingStates.remove(clientUuid);
            TelegramProfileUpdateBiometricRequest request = telegramProfileParser.parseToTelegramProfileUpdateBiometricRequest(chatId, clientUuid, text);
            return telegramProfileResolver.biometricUpdate(request);
        } else if (commandResolver.isProfileSettingsUpdateCommand(waitingStates.get(clientUuid), language)) {
            waitingStates.remove(clientUuid);
            TelegramProfileUpdateSettingsRequest request = telegramProfileParser.parseToTelegramProfileUpdateSettingsRequest(chatId, email, text);
            return telegramProfileResolver.settingsUpdate(request);
        } else if (commandResolver.isNotificationSubscriptionDietAdd(waitingStates.get(clientUuid), language)) {
            waitingStates.remove(clientUuid);
            AddDietNotificationRequest request = new AddDietNotificationRequest(clientUuid, chatId, email, telegramId, text);
            return telegramDietNotificationSubscriptionResolver.add(request);
        } else if (commandResolver.isNotificationSubscriptionDietDelete(waitingStates.get(clientUuid), language)) {
            waitingStates.remove(clientUuid);
            DeleteDietNotificationRequest request = new DeleteDietNotificationRequest(clientUuid, email, chatId, telegramId, text);
            return telegramDietNotificationSubscriptionResolver.delete(request);
        } else if (commandResolver.isStatisticHistoryCommand(waitingStates.get(clientUuid), language)) {
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
            return baseMessagesFactory.getInvalidAuthCode(chatId, Language.EN);
        }
    }

    private BotApiMethod<?> authenticateTelegramClient(String text,
                                                       String chatId,
                                                       Long telegramId) {
        telegramAuthService.authenticateTelegramClient(text, telegramId, chatId);
        String welcomeMessage = messageResolveService.message(MessageResolverCodes.TELEGRAM_WELCOME_MESSAGE_CODE, Language.EN);
        return baseMessagesFactory.getMainMenuKeyboard(chatId, welcomeMessage, Language.EN);
    }

    private String getChatId(Message message) {
        return String.valueOf(message.getChatId());
    }


    public void checkIsBlockedAndThrow(String email) {
        if (blockingService.isBlocked(email)) {
            throw new BlockedCredentialsException("Client with email is blocked");
        }
    }
}
