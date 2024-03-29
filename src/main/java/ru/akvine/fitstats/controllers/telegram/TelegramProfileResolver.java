package ru.akvine.fitstats.controllers.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.akvine.fitstats.controllers.telegram.converters.TelegramProfileConverter;
import ru.akvine.fitstats.controllers.telegram.dto.common.TelegramBaseRequest;
import ru.akvine.fitstats.controllers.telegram.dto.profile.TelegramProfileUpdateBiometricRequest;
import ru.akvine.fitstats.controllers.telegram.dto.profile.TelegramProfileUpdateSettingsRequest;
import ru.akvine.fitstats.controllers.telegram.validators.TelegramProfileValidator;
import ru.akvine.fitstats.services.dto.client.BiometricBean;
import ru.akvine.fitstats.services.dto.client.ClientSettingsBean;
import ru.akvine.fitstats.services.dto.profile.UpdateBiometric;
import ru.akvine.fitstats.services.dto.profile.UpdateSettings;
import ru.akvine.fitstats.services.profile.ProfileService;

@Component
@RequiredArgsConstructor
public class TelegramProfileResolver {
    private final ProfileService profileService;
    private final TelegramProfileConverter telegramProfileConverter;
    private final TelegramProfileValidator telegramProfileValidator;

    public SendMessage biometricDisplay(TelegramBaseRequest request) {
        BiometricBean biometricBean = profileService.display(request.getClientUuid());
        return telegramProfileConverter.convertToProfileBiometricDisplayResponse(request.getChatId(), biometricBean);
    }

    public SendMessage biometricUpdate(TelegramProfileUpdateBiometricRequest request) {
        telegramProfileValidator.verifyTelegramProfileUpdateBiometricRequest(request);
        UpdateBiometric updateBiometric = telegramProfileConverter.convertToProfileUpdateBiometric(request);
        BiometricBean biometricBean = profileService.updateBiometric(updateBiometric);
        return telegramProfileConverter.convertToProfileUpdateBiometricResponse(request.getChatId(), biometricBean);
    }

    public SendMessage settingsList(TelegramBaseRequest request) {
        ClientSettingsBean settingsBean = profileService.listSettingsByUuid(request.getClientUuid());
        return telegramProfileConverter.convertToProfileSettingsResponse(request.getChatId(), settingsBean);
    }

    public SendMessage settingsUpdate(TelegramProfileUpdateSettingsRequest request) {
        telegramProfileValidator.verifyTelegramProfileUpdateSettingsRequest(request);
        UpdateSettings updateSettings = telegramProfileConverter.convertToUpdateSettings(request);
        ClientSettingsBean clientSettingsBean = profileService.updateSettings(updateSettings);
        return telegramProfileConverter.convertToProfileSettingsResponse(request.getChatId(), clientSettingsBean);
    }
}
