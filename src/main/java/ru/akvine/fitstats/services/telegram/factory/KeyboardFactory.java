package ru.akvine.fitstats.services.telegram.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.akvine.fitstats.constants.MessageResolverCodes;
import ru.akvine.fitstats.enums.Language;
import ru.akvine.fitstats.services.MessageResolveService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class KeyboardFactory {
    private final MessageResolveService messageResolveService;

    public ReplyKeyboardMarkup getMainMenuKeyboard(Language language) {
        KeyboardRow firstRow = new KeyboardRow();
        KeyboardRow secondRow = new KeyboardRow();
        firstRow.add(new KeyboardButton(messageResolveService.message(MessageResolverCodes.TELEGRAM_PRODUCT_CODE, language)));
        firstRow.add(new KeyboardButton(messageResolveService.message(MessageResolverCodes.TELEGRAM_DIET_CODE, language)));
        firstRow.add(new KeyboardButton(messageResolveService.message(MessageResolverCodes.TELEGRAM_STATISTIC_CODE, language)));
        secondRow.add(new KeyboardButton(messageResolveService.message(MessageResolverCodes.TELEGRAM_NOTIFICATION_SUBSCRIPTION_CODE, language)));
        secondRow.add(new KeyboardButton(messageResolveService.message(MessageResolverCodes.TELEGRAM_PROFILE_CODE, language)));
        secondRow.add(new KeyboardButton(messageResolveService.message(MessageResolverCodes.TELEGRAM_HELP_CODE, language)));

        return getKeyboard(List.of(firstRow, secondRow));
    }

    public ReplyKeyboardMarkup getNotificationSubscriptionTypesKeyboard(Language language) {
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton(messageResolveService.message(MessageResolverCodes.TELEGRAM_NOTIFICATION_SUBSCRIPTION_DIET_CODE, language)));
        row.add(new KeyboardButton(messageResolveService.message(MessageResolverCodes.TELEGRAM_BACK_CODE, language)));

        return getKeyboard(List.of(row));
    }

    public ReplyKeyboardMarkup getDietNotificationSubscriptionKeyboard(Language language) {
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton(messageResolveService.message(MessageResolverCodes.TELEGRAM_NOTIFICATION_SUBSCRIPTION_DIET_ADD_CODE, language)));
        row.add(new KeyboardButton(messageResolveService.message(MessageResolverCodes.TELEGRAM_NOTIFICATION_SUBSCRIPTION_DIET_LIST_CODE, language)));
        row.add(new KeyboardButton(messageResolveService.message(MessageResolverCodes.TELEGRAM_NOTIFICATION_SUBSCRIPTION_DIET_DELETE_CODE, language)));
        row.add(new KeyboardButton(messageResolveService.message(MessageResolverCodes.TELEGRAM_BACK_CODE, language)));

        return getKeyboard(List.of(row));
    }

    public ReplyKeyboardMarkup getProductsKeyboard(Language language) {
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton(messageResolveService.message(MessageResolverCodes.TELEGRAM_PRODUCT_ADD_CODE, language)));
        row.add(new KeyboardButton(messageResolveService.message(MessageResolverCodes.TELEGRAM_PRODUCT_LIST_CODE, language)));
        row.add(new KeyboardButton(messageResolveService.message(MessageResolverCodes.TELEGRAM_BACK_CODE, language)));

        return getKeyboard(List.of(row));
    }

    public ReplyKeyboardMarkup getDietKeyboard(Language language) {
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton(messageResolveService.message(MessageResolverCodes.TELEGRAM_DIET_ADD_CODE, language)));
        row.add(new KeyboardButton(messageResolveService.message(MessageResolverCodes.TELEGRAM_DIET_LIST_CODE, language)));
        row.add(new KeyboardButton(messageResolveService.message(MessageResolverCodes.TELEGRAM_DIET_DELETE_CODE, language)));
        row.add(new KeyboardButton(messageResolveService.message(MessageResolverCodes.TELEGRAM_DIET_STATISTIC_DISPLAY_CODE, language)));
        row.add(new KeyboardButton(messageResolveService.message(MessageResolverCodes.TELEGRAM_BACK_CODE, language)));

        return getKeyboard(List.of(row));
    }

    public ReplyKeyboardMarkup getProfileKeyboard(Language language) {
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton(messageResolveService.message(MessageResolverCodes.TELEGRAM_PROFILE_BIOMETRIC_DISPLAY_CODE, language)));
        row.add(new KeyboardButton(messageResolveService.message(MessageResolverCodes.TELEGRAM_PROFILE_UPDATE_CODE, language)));
        row.add(new KeyboardButton(messageResolveService.message(MessageResolverCodes.TELEGRAM_BACK_CODE, language)));

        return getKeyboard(List.of(row));
    }

    public ReplyKeyboardMarkup getStatisticKeyboard(Language language) {
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton(messageResolveService.message(MessageResolverCodes.TELEGRAM_STATISTIC_HISTORY_CODE, language)));
        row.add(new KeyboardButton(messageResolveService.message(MessageResolverCodes.TELEGRAM_STATISTIC_INDICATORS_CODE, language)));
        row.add(new KeyboardButton(messageResolveService.message(MessageResolverCodes.TELEGRAM_BACK_CODE, language)));

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
