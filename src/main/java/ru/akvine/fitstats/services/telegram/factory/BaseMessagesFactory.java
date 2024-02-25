package ru.akvine.fitstats.services.telegram.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.akvine.fitstats.constants.MessageResolverCodes;
import ru.akvine.fitstats.enums.Language;
import ru.akvine.fitstats.services.MessageResolveService;

@Component
@RequiredArgsConstructor
public class BaseMessagesFactory {
    private final KeyboardFactory keyboardFactory;
    private final MessageResolveService messageResolveService;

    public SendMessage getInvalidMessage(String chatId, Language language) {
        return new SendMessage(chatId,
                messageResolveService.message(MessageResolverCodes.TELEGRAM_UNKNOWN_COMMAND_MESSAGE_CODE, language));
    }

    public SendMessage getHelpMessage(String chatId, Language language) {
        return new SendMessage(
                chatId,
                messageResolveService.message(MessageResolverCodes.TELEGRAM_LIST_AVAILABLE_COMMANDS_CODE, language)
        );
    }

    public SendMessage getProductAddInputWaiting(String chatId, Language language) {
        return new SendMessage(
                chatId,
                messageResolveService.message(MessageResolverCodes.TELEGRAM_PRODUCT_INPUT_ADD_CODE, language)
        );
    }

    public SendMessage getProductListFilter(String chatId, Language language) {
        return new SendMessage(
                chatId,
                messageResolveService.message(MessageResolverCodes.TELEGRAM_PRODUCT_INPUT_FILTER_CODE, language)
        );
    }

    public SendMessage getDietKeyboard(String chatId, Language language) {
        SendMessage sendMessage = new SendMessage(
                chatId,
                messageResolveService.message(MessageResolverCodes.TELEGRAM_CHOOSE_ACTION_CODE, language) + ": "
        );
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(keyboardFactory.getDietKeyboard(language));
        return sendMessage;
    }

    public SendMessage getDietRecordAddInputWaiting(String chatId, Language language) {
        return new SendMessage(
                chatId,
                messageResolveService.message(MessageResolverCodes.TELEGRAM_DIET_RECORD_ADD_CODE, language)
        );
    }

    public SendMessage getDietRecordDeleteInputWaiting(String chatId, Language language) {
        return new SendMessage(
                chatId,
                messageResolveService.message(MessageResolverCodes.TELEGRAM_DIET_RECORD_DELETE_CODE, language)
        );
    }

    public SendMessage getProfileKeyboard(String chatId, Language language) {
        SendMessage sendMessage = new SendMessage(
                chatId,
                messageResolveService.message(MessageResolverCodes.TELEGRAM_CHOOSE_ACTION_CODE, language) + ": "
        );
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(keyboardFactory.getProfileKeyboard(language));
        return sendMessage;
    }

    public SendMessage getProfileUpdateBiometricInputWaiting(String chatId, Language language) {
        return new SendMessage(
                chatId,
                messageResolveService.message(MessageResolverCodes.TELEGRAM_PROFILE_BIOMETRIC_INPUT_NEW_DATA_CODE, language)
        );
    }

    public SendMessage getProfileSettingsUpdateInputWaiting(String chatId, Language language) {
        return new SendMessage(
                chatId,
                messageResolveService.message(MessageResolverCodes.TELEGRAM_PROFILE_SETTINGS_INPUT_NEW_DATA_CODE, language)
        );
    }

    public SendMessage getNotificationSubscriptionDietAdd(String chatId, Language language) {
        return new SendMessage(
                chatId,
                messageResolveService.message(MessageResolverCodes.TELEGRAM_INPUT_SUBSCRIPTION_TYPE_FOR_ADD_CODE, language)
        );
    }

    public SendMessage getNotificationSubscriptionDietDelete(String chatId, Language language) {
        return new SendMessage(
                chatId,
                messageResolveService.message(MessageResolverCodes.TELEGRAM_INPUT_SUBSCRIPTION_TYPE_FOR_DELETE_CODE, language)
        );
    }

    public SendMessage getNotificationSubscriptionTypesKeyboard(String chatId, Language language) {
        SendMessage sendMessage = new SendMessage(
                chatId,
                messageResolveService.message(MessageResolverCodes.TELEGRAM_CHOOSE_SUBSCRIPTION_TYPE_CODE, language)
        );
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(keyboardFactory.getNotificationSubscriptionTypesKeyboard(language));
        return sendMessage;
    }

    public SendMessage getProductKeyboard(String chatId, Language language) {
        SendMessage sendMessage = new SendMessage(
                chatId,
                messageResolveService.message(MessageResolverCodes.TELEGRAM_CHOOSE_ACTION_CODE, language) + ": "
        );
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(keyboardFactory.getProductsKeyboard(language));
        return sendMessage;
    }

    public SendMessage getDietNotificationSubscriptionKeyboard(String chatId, Language language) {
        SendMessage sendMessage = new SendMessage(
                chatId,
                messageResolveService.message(MessageResolverCodes.TELEGRAM_CHOOSE_ACTION_CODE, language) + ": "
        );
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(keyboardFactory.getDietNotificationSubscriptionKeyboard(language));
        return sendMessage;
    }

    public SendMessage getStatisticHistoryInputWaiting(String chatId, Language language) {
        return new SendMessage(
                chatId,
                messageResolveService.message(MessageResolverCodes.TELEGRAM_STATISTIC_HISTORY_INPUT_WAITING_CODE, language)
        );
    }

    public SendMessage getStatisticKeyboard(String chatId, Language language) {
        SendMessage sendMessage = new SendMessage(
                chatId,
                messageResolveService.message(MessageResolverCodes.TELEGRAM_CHOOSE_ACTION_CODE, language) + ": "
        );
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(keyboardFactory.getStatisticKeyboard(language));
        return sendMessage;
    }

    public SendMessage getMainMenuKeyboard(String chatId,
                                           String message,
                                           Language language) {
        SendMessage sendMessage = new SendMessage(
                chatId,
                message
        );
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(keyboardFactory.getMainMenuKeyboard(language));
        return sendMessage;
    }

    public SendMessage getInvalidAuthCode(String chatId, Language language) {
        return new SendMessage(
                chatId,
                messageResolveService.message(MessageResolverCodes.TELEGRAM_INVALID_AUTH_CODE, language)
        );
    }
}
