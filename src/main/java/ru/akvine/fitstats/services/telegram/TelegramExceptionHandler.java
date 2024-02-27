package ru.akvine.fitstats.services.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.akvine.fitstats.constants.MessageResolverCodes;
import ru.akvine.fitstats.context.ClientSettingsContext;
import ru.akvine.fitstats.enums.Language;
import ru.akvine.fitstats.exceptions.barcode.BarCodeScanException;
import ru.akvine.fitstats.exceptions.diet.DietRecordsNotUniqueResultException;
import ru.akvine.fitstats.exceptions.diet.ProductsNotUniqueResultException;
import ru.akvine.fitstats.exceptions.product.ProductNotFoundException;
import ru.akvine.fitstats.exceptions.security.BlockedCredentialsException;
import ru.akvine.fitstats.exceptions.telegram.TelegramLoadPhotoException;
import ru.akvine.fitstats.exceptions.telegram.parse.*;
import ru.akvine.fitstats.services.MessageResolveService;

@Component
@RequiredArgsConstructor
public class TelegramExceptionHandler {
    private final MessageResolveService messageResolveService;

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
        } else if (exception instanceof BlockedCredentialsException) {
            return handleBlockedCredentialsException(chatId);
        } else if (exception instanceof BarCodeScanException) {
            return handleBarCodeScanException(chatId);
        } else {
            return handleException(chatId);
        }
    }

    private SendMessage handleException(String chatId) {
        Language language = ClientSettingsContext.getClientSettingsContextHolder().getBySessionForCurrent().getLanguage();
        return new SendMessage(chatId, messageResolveService.message(MessageResolverCodes.TELEGRAM_GENERAL_ERROR_CODE, language));
    }

    private SendMessage handleProductNotFoundException(String chatId) {
        Language language = ClientSettingsContext.getClientSettingsContextHolder().getBySessionForCurrent().getLanguage();
        return new SendMessage(chatId,  messageResolveService.message(MessageResolverCodes.TELEGRAM_PRODUCT_NOT_FOUND_ERROR_CODE, language));
    }

    private SendMessage handleTelegramFatsParseException(String chatId) {
        Language language = ClientSettingsContext.getClientSettingsContextHolder().getBySessionForCurrent().getLanguage();
        return new SendMessage(chatId, messageResolveService.message(MessageResolverCodes.TELEGRAM_MACRONUTRIENT_FATS_INPUT_ERROR_CODE, language));
    }

    private SendMessage handleTelegramProteinsParseException(String chatId) {
        Language language = ClientSettingsContext.getClientSettingsContextHolder().getBySessionForCurrent().getLanguage();
        return new SendMessage(chatId, messageResolveService.message(MessageResolverCodes.TELEGRAM_MACRONUTRIENT_PROTEINS_INPUT_ERROR_CODE, language));
    }

    private SendMessage handleTelegramCarbohydratesParseException(String chatId) {
        Language language = ClientSettingsContext.getClientSettingsContextHolder().getBySessionForCurrent().getLanguage();
        return new SendMessage(chatId, messageResolveService.message(MessageResolverCodes.TELEGRAM_MACRONUTRIENT_CARBOHYDRATES_INPUT_ERROR_CODE, language));
    }

    private SendMessage handleTelegramVolParseException(String chatId) {
        Language language = ClientSettingsContext.getClientSettingsContextHolder().getBySessionForCurrent().getLanguage();
        return new SendMessage(chatId, messageResolveService.message(MessageResolverCodes.TELEGRAM_MACRONUTRIENT_VOL_INPUT_ERROR_CODE, language));
    }

    private SendMessage handleProductsNotUniqueException(String chatId) {
        Language language = ClientSettingsContext.getClientSettingsContextHolder().getBySessionForCurrent().getLanguage();
        return new SendMessage(chatId, messageResolveService.message(MessageResolverCodes.TELEGRAM_PRODUCT_UUID_NOT_UNIQUE_ERROR_CODE, language));
    }
    private SendMessage handleDietRecordsNotUniqueException(String chatId) {
        Language language = ClientSettingsContext.getClientSettingsContextHolder().getBySessionForCurrent().getLanguage();
        return new SendMessage(chatId, messageResolveService.message(MessageResolverCodes.TELEGRAM_DIET_UUID_NOT_UNIQUE_ERROR_CODE, language));
    }

    private SendMessage handleBlockedCredentialsException(String chatId) {
        Language language = ClientSettingsContext.getClientSettingsContextHolder().getBySessionForCurrent().getLanguage();
        return new SendMessage(chatId, messageResolveService.message(MessageResolverCodes.TELEGRAM_BLOCKED_CREDENTIALS_ERROR_CODE, language));
    }

    private SendMessage handleBarCodeScanException(String chatId) {
        Language language = ClientSettingsContext.getClientSettingsContextHolder().getBySessionForCurrent().getLanguage();
        return new SendMessage(chatId, messageResolveService.message(MessageResolverCodes.TELEGRAM_BARCODE_SCAN_ERROR_CODE, language));
    }
}
