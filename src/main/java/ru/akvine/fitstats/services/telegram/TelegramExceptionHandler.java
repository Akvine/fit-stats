package ru.akvine.fitstats.services.telegram;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.akvine.fitstats.exceptions.diet.DietRecordsNotUniqueResultException;
import ru.akvine.fitstats.exceptions.diet.ProductsNotUniqueResultException;
import ru.akvine.fitstats.exceptions.product.ProductNotFoundException;
import ru.akvine.fitstats.exceptions.telegram.parse.*;

@Component
public class TelegramExceptionHandler {
    public SendMessage handle(String chatId, Exception exception) {
        if (exception instanceof ProductNotFoundException) {
            return handleProductNotFoundException(chatId);
        } else if (exception instanceof TelegramFatsParseException) {
            return handleTelegramFatsParseException(chatId);
        } else if (exception instanceof TelegramProteinsParseException) {
            return handleTelegramProteinsParseException(chatId);
        } else if (exception instanceof TelegramCarbohydratesParseException) {
            return handleTelegramCarbohydratesParseException(chatId);
        } else if (exception instanceof TelegramVolParseException) {
            return handleTelegramVolParseException(chatId);
        } else if (exception instanceof ProductsNotUniqueResultException) {
            return handleProductsNotUniqueException(chatId);
        } else if (exception instanceof DietRecordsNotUniqueResultException) {
          return handleDietRecordsNotUniqueException(chatId);
        } else {
            return handleException(chatId);
        }
    }

    private SendMessage handleException(String chatId) {
        return new SendMessage(chatId, "Что-то пошло не так...");
    }

    private SendMessage handleProductNotFoundException(String chatId) {
        return new SendMessage(chatId, "Продукт с указанным uuid не найден!");
    }

    private SendMessage handleTelegramFatsParseException(String chatId) {
        return new SendMessage(chatId, "Некорректно введены жиры!");
    }

    private SendMessage handleTelegramProteinsParseException(String chatId) {
        return new SendMessage(chatId, "Некорректно введены белки!");
    }

    private SendMessage handleTelegramCarbohydratesParseException(String chatId) {
        return new SendMessage(chatId, "Некорректно введены углеводы!");
    }

    private SendMessage handleTelegramVolParseException(String chatId) {
        return new SendMessage(chatId, "Некорректно введен процент крепости алкоголя!");
    }

    private SendMessage handleProductsNotUniqueException(String chatId) {
        return new SendMessage(chatId, "Данному uuid соответствует не один продукт. Введите uuid по длиннее");
    }
    private SendMessage handleDietRecordsNotUniqueException(String chatId) {
        return new SendMessage(chatId, "Данному uuid соответствует не одна запись. Введите uuid по длиннее");
    }
}
