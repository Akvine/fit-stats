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
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton("Список продуктов"));
        row.add(new KeyboardButton("БЖУ за сегодня"));
        row.add(new KeyboardButton("Добавить запись"));
        row.add(new KeyboardButton("Помощь"));

        return getKeyboard(row);
    }

    private ReplyKeyboardMarkup getKeyboard(KeyboardRow row) {
        List<KeyboardRow> keyboard = List.of(row);

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        return replyKeyboardMarkup;
    }
}
