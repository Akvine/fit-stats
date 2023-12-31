package ru.akvine.fitstats.services.telegram.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Component
@RequiredArgsConstructor
public class KeyboardFactory {

    public ReplyKeyboardMarkup getMainMenuKeyboard() {
        KeyboardRow firstRow = new KeyboardRow();
        KeyboardRow secondRow = new KeyboardRow();
        firstRow.add(new KeyboardButton("Продукты"));
        firstRow.add(new KeyboardButton("Диетический дневник"));
        firstRow.add(new KeyboardButton("Статистика"));
        secondRow.add(new KeyboardButton("Уведомления"));
        secondRow.add(new KeyboardButton("Профиль"));
        secondRow.add(new KeyboardButton("Помощь"));

        return getKeyboard(List.of(firstRow, secondRow));
    }

    public ReplyKeyboardMarkup getNotificationSubscriptionTypesKeyboard() {
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton("Диета"));;
        row.add(new KeyboardButton("Назад"));

        return getKeyboard(List.of(row));
    }

    public ReplyKeyboardMarkup getDietNotificationSubscriptionKeyboard() {
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton("Добавить подписку на уведомления"));
        row.add(new KeyboardButton("Список подписок на уведомления"));
        row.add(new KeyboardButton("Удалить подписку на уведомление"));
        row.add(new KeyboardButton("Назад"));

        return getKeyboard(List.of(row));
    }

    public ReplyKeyboardMarkup getProductsKeyboard() {
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton("Добавить продукт"));
        row.add(new KeyboardButton("Список продуктов"));
        row.add(new KeyboardButton("Назад"));

        return getKeyboard(List.of(row));
    }

    public ReplyKeyboardMarkup getDietKeyboard() {
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton("Добавить запись"));
        row.add(new KeyboardButton("Список записей"));
        row.add(new KeyboardButton("Удалить запись"));
        row.add(new KeyboardButton("КБЖУ за сегодня"));
        row.add(new KeyboardButton("Назад"));

        return getKeyboard(List.of(row));
    }

    public ReplyKeyboardMarkup getProfileKeyboard() {
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton("Биометрические показатели"));
        row.add(new KeyboardButton("Обновить показатели"));
        row.add(new KeyboardButton("Назад"));

        return getKeyboard(List.of(row));
    }

    public ReplyKeyboardMarkup getStatisticKeyboard() {
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton("История"));
        row.add(new KeyboardButton("Основные показатели"));
        row.add(new KeyboardButton("Назад"));

        return getKeyboard(List.of(row));
    }

    private ReplyKeyboardMarkup getKeyboard(List<KeyboardRow> rows) {

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(rows);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        return replyKeyboardMarkup;
    }
}
