package ru.akvine.fitstats.controllers.telegram.parser;

import com.google.common.base.Preconditions;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.telegram.dto.profile.TelegramProfileUpdateBiometricRequest;
import ru.akvine.fitstats.controllers.telegram.dto.profile.TelegramProfileUpdateSettingsRequest;
import ru.akvine.fitstats.exceptions.telegram.parse.TelegramAgeParseException;

@Component
public class TelegramProfileParser {
    private static final String COMMA = ",";

    public TelegramProfileUpdateBiometricRequest parseToTelegramProfileUpdateBiometricRequest(String chatId, String clientUuid, String text) {
        Preconditions.checkNotNull(text, "text is null");
        Preconditions.checkNotNull(clientUuid, "clientUuid is null");

        String[] parts = text.split(COMMA);
        int age;

        try {
            age = Integer.parseInt(parts[0].trim());
        } catch (NumberFormatException exception) {
            throw new TelegramAgeParseException("Age parse error. Age is invalid!");
        }

        String height = parts[1].trim();
        String weight = parts[2].trim();
        String physicalActivity = parts[3].trim();

        boolean updateDietSetting = Boolean.parseBoolean(parts[4].trim());

        return (TelegramProfileUpdateBiometricRequest) new TelegramProfileUpdateBiometricRequest()
                .setAge(age)
                .setHeight(height)
                .setWeight(weight)
                .setPhysicalActivity(physicalActivity)
                .setUpdateDietSetting(updateDietSetting)
                .setClientUuid(clientUuid)
                .setChatId(chatId);
    }

    public TelegramProfileUpdateSettingsRequest parseToTelegramProfileUpdateSettingsRequest(String chatId, String clientEmail, String text) {
        Preconditions.checkNotNull(text, "text is null");
        Preconditions.checkNotNull(clientEmail, "clientEmail is null");

        String[] parts = text.split(COMMA);

        return (TelegramProfileUpdateSettingsRequest) new TelegramProfileUpdateSettingsRequest()
                .setRoundAccuracy(parts[0].trim())
                .setLanguage(parts[1].trim())
                .setPrintMode(parts[2].trim())
                .setClientEmail(clientEmail)
                .setChatId(chatId);
    }
}
