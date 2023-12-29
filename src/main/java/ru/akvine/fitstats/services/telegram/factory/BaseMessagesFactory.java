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

    public SendMessage getProductAddInputWaiting(String chatId) {
        return new SendMessage(
                chatId,
                "Введите название продукта, производителя, жиры (на 100 грамм)," +
                        " белки (на 100 грамм), углеводы (на 100 грамм), процент крепости алкоголя, измерение, категории " +
                        "(Пример: Творог, ООО Савушкин, 2.5, 16, 1.2, 0, g, Творожные): "
        );
    }

    public SendMessage getProductListFilter(String chatId) {
        return new SendMessage(
                chatId,
                "Введите фильтр, по которому мы найдем продукты: "
        );
    }

    public SendMessage getDietKeyboard(String chatId) {
        SendMessage sendMessage = new SendMessage(
                chatId,
                "Выберите действие: "
        );
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(keyboardFactory.getDietKeyboard());
        return sendMessage;
    }

    public SendMessage getDietRecordAddInputWaiting(String chatId) {
        return new SendMessage(
                chatId,
                "Введите uuid продукта и объем, который вы потребили: "
        );
    }

    public SendMessage getDietRecordDeleteInputWaiting(String chatId) {
        return new SendMessage(
                chatId,
                "Введите uuid записи, которую собираетесь удалить: "
        );
    }

    public SendMessage getProfileKeyboard(String chatId) {
        SendMessage sendMessage = new SendMessage(
                chatId,
                "Выберите действие: "
        );
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(keyboardFactory.getProfileKeyboard());
        return sendMessage;
    }

    public SendMessage getProfileUpdateBiometricInputWaiting(String chatId) {
        return new SendMessage(
                chatId,
                "Введите новые биометрические данные (возраст, рост, вес, физическую активность, тип диеты): "
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

    public SendMessage getProductKeyboard(String chatId) {
        SendMessage sendMessage = new SendMessage(
                chatId,
                "Выберите действие: "
        );
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(keyboardFactory.getProductsKeyboard());
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

    public SendMessage getStatisticHistoryInputWaiting(String chatId) {
        return new SendMessage(
                chatId,
                "Введите продолжительность (DAY, MONTH, WEEK, YEAR) и макронутриент (FATS, PROTEINS, CARBOHYDRATES, CALORIES): "
        );
    }

    public SendMessage getStatisticKeyboard(String chatId) {
        SendMessage sendMessage = new SendMessage(
                chatId,
                "Выберите действие: "
        );
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(keyboardFactory.getStatisticKeyboard());
        return sendMessage;
    }

    public SendMessage getMainMenuKeyboard(String chatId, String message) {
        SendMessage sendMessage = new SendMessage(
                chatId,
                message
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
