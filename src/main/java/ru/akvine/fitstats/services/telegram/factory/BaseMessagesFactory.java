package ru.akvine.fitstats.services.telegram.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@RequiredArgsConstructor
public class BaseMessagesFactory {
    private final KeyboardFactory keyboardFactory;

    public SendMessage getInvalidMessage(String chatId) {
        return new SendMessage(chatId, "Незнаю такой команды! Справка: /help");
    }

    public SendMessage getHelpMessage(String chatId) {
        return new SendMessage(
                chatId,
                "Список доступных команд:\n" +
                        "1. /start - начать работу с ботом\n" +
                        "2. /help - помощь\n" +
                        "3. /products/list - найти продукты по фильтру\n" +
                        "4. /diet/display - показать БЖУ на сегодня\n" +
                        "5. /diet/record/add - добавить запись\n"
        );
    }

    public SendMessage getProductListFilter(String chatId) {
        return new SendMessage(
                chatId,
                "Введите фильтр, по которому мы найдем продукты: "
                );
    }

    public SendMessage getDietRecordAddInputWaiting(String chatId) {
        return new SendMessage(
                chatId,
                "Введите uuid продукта и объем, который вы потребили: "
        );
    }

    public SendMessage getNotificationSubscriptionDietAdd(String chatId) {
        return new SendMessage(
                chatId,
                "Введите тип уведомления для отслеживания (proteins, fats, carbohydrates, energy):"
        );
    }

    public SendMessage getNotificationSubscriptionDietDelete(String chatId) {
        return new SendMessage(
                chatId,
                "Введите тип уведомления для удаления (proteins, fats, carbohydrates, energy):"
        );
    }

    public SendMessage getNotificationSubscriptionTypesKeyboard(String chatId) {
        SendMessage sendMessage = new SendMessage(
                chatId,
                "Выберите тип подписки: "
        );
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(keyboardFactory.getNotificationSubscriptionTypesKeyboard());
        return sendMessage;
    }

    public SendMessage getDietNotificationSubscriptionKeyboard(String chatId) {
        SendMessage sendMessage = new SendMessage(
                chatId,
                "Выберите действие: "
        );
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(keyboardFactory.getDietNotificationSubscriptionKeyboard());
        return sendMessage;
    }

    public SendMessage getMainMenuKeyboard(String chatId) {
        SendMessage sendMessage = new SendMessage(
                chatId,
                "Добро пожаловать! Вам доступен полный функционал бота Fit-Stats"
        );
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(keyboardFactory.getMainMenuKeyboard());
        return sendMessage;
    }

    public SendMessage getInvalidAuthCode(String chatId) {
        return new SendMessage(
                chatId,
                "Неверный код!"
        );
    }
}
