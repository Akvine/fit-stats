package ru.akvine.fitstats.services.telegram.handler;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.akvine.fitstats.exceptions.telegram.TelegramSubscriptionNotFoundException;
import ru.akvine.fitstats.services.dto.client.ClientBean;
import ru.akvine.fitstats.services.telegram.CommandResolver;
import ru.akvine.fitstats.services.telegram.factory.BaseMessagesFactory;

@Component
@RequiredArgsConstructor
@Slf4j
public class CallbackQueryMessageHandler extends AbstractMessageHandler {
    private final CommandResolver commandResolver;
    private final BaseMessagesFactory messageFactory;

    @Override
    public BotApiMethod<?> processUpdate(Update update) {
        Preconditions.checkNotNull(update, "update is null");
        Preconditions.checkNotNull(update.getCallbackQuery(), "update.callbackQuery is null");
        Preconditions.checkNotNull(update.getCallbackQuery().getMessage(), "update.callbackQuery.message is null");

        CallbackQuery callbackQuery = update.getCallbackQuery();

        try {
            ClientBean client = getAuthenticateUser(update);
            logger.debug("Client=[{}] authenticate with telegramId=[{}]", client.getId(),  getTelegramId(update));
            return processAuthenticateUserUpdate(update);
        } catch (TelegramSubscriptionNotFoundException nfe) {
            logger.debug("No authenticate client with telegramId=" + getTelegramId(update));
            return messageFactory.getStartMessage(getChatId(callbackQuery));
        }
    }

    private BotApiMethod<?> processAuthenticateUserUpdate(Update update) {
        String data = update.getCallbackQuery().getData();
        String chatId = getChatId(update.getCallbackQuery());

        if (commandResolver.isStopAcceptCommand(data)) {
            telegramSubscriptionService.delete(getTelegramId(update), TELEGRAM_SUBSCRIPTION_TYPE_ID);
            return messageFactory.getStopAcceptMessage(chatId);
        }
        if (commandResolver.isStopCancelCommand(data)) {
            return messageFactory.getStopCancelMessage(chatId);
        }

        return messageFactory.getHelpMessage(chatId);
    }

    @Override
    protected Long getTelegramId(Update update) {
        return update.getCallbackQuery().getFrom().getId();
    }

    private String getChatId(CallbackQuery callbackQuery) {
        return String.valueOf(callbackQuery.getMessage().getChatId());
    }
}
