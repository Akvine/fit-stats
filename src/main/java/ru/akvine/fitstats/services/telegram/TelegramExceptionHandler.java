package ru.akvine.fitstats.services.telegram;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.akvine.fitstats.exceptions.product.ProductNotFoundException;

@Component
public class TelegramExceptionHandler {
    public SendMessage handle(String chatId, Exception exception) {
        if (exception instanceof ProductNotFoundException) {
            return handleProductNotFoundException(chatId);
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
}
