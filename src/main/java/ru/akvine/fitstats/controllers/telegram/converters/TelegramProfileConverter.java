package ru.akvine.fitstats.controllers.telegram.converters;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.akvine.fitstats.constants.MessageResolverCodes;
import ru.akvine.fitstats.context.ClientSettingsContext;
import ru.akvine.fitstats.controllers.telegram.dto.profile.TelegramProfileUpdateBiometricRequest;
import ru.akvine.fitstats.controllers.telegram.dto.profile.TelegramProfileUpdateSettingsRequest;
import ru.akvine.fitstats.enums.Language;
import ru.akvine.fitstats.enums.PhysicalActivity;
import ru.akvine.fitstats.enums.telegram.PrintMacronutrientsMode;
import ru.akvine.fitstats.services.MessageResolveService;
import ru.akvine.fitstats.services.dto.client.BiometricBean;
import ru.akvine.fitstats.services.dto.client.ClientSettingsBean;
import ru.akvine.fitstats.services.dto.profile.UpdateBiometric;
import ru.akvine.fitstats.services.dto.profile.UpdateSettings;

@Component
@RequiredArgsConstructor
public class TelegramProfileConverter {
    private final static String NEXT_LINE = "\n";

    private final MessageResolveService messageResolveService;

    public SendMessage convertToProfileBiometricDisplayResponse(String chatId, BiometricBean bean) {
        Preconditions.checkNotNull(bean, "biometricBean is null");
        return new SendMessage(
                chatId,
                buildProfileBiometricDisplayResponse(bean)
        );
    }

    public UpdateBiometric convertToProfileUpdateBiometric(TelegramProfileUpdateBiometricRequest request) {
        Preconditions.checkNotNull(request, "telegramProfileUpdateBiometricRequest is null");
        return new UpdateBiometric()
                .setClientUuid(request.getClientUuid())
                .setAge(request.getAge())
                .setHeight(request.getHeight())
                .setWeight(request.getWeight())
                .setPhysicalActivity(PhysicalActivity.valueOf(request.getPhysicalActivity()))
                .setUpdateDietSetting(request.isUpdateDietSetting());
    }

    public UpdateSettings convertToUpdateSettings(TelegramProfileUpdateSettingsRequest request) {
        Preconditions.checkNotNull(request, "telegramProfileUpdateSettingsRequest is null");
        return new UpdateSettings()
                .setEmail(request.getClientEmail())
                .setRoundAccuracy(Integer.parseInt(request.getRoundAccuracy()))
                .setLanguage(Language.valueOf(request.getLanguage().toUpperCase()))
                .setPrintMacronutrientsMode(PrintMacronutrientsMode.valueOf(request.getPrintMode().toUpperCase()));
    }

    public SendMessage convertToProfileUpdateBiometricResponse(String chatId, BiometricBean biometricBean) {
        Preconditions.checkNotNull(biometricBean, "biometricBean is null");
        return new SendMessage(
                chatId,
                buildProfileBiometricDisplayResponse(biometricBean)
        );
    }

    public SendMessage convertToProfileSettingsResponse(String chatId, ClientSettingsBean clientSettingsBean) {
        Preconditions.checkNotNull(clientSettingsBean, "clientSettingsBean is null");
        Language language = ClientSettingsContext.getClientSettingsContextHolder().getBySessionForCurrent().getLanguage();
        StringBuilder sb = new StringBuilder();
        sb
                .append(messageResolveService.message(MessageResolverCodes.ROUND_ACCURACY_CODE, language))
                .append(": ")
                .append(clientSettingsBean.getRoundAccuracy())
                .append(NEXT_LINE);
        sb
                .append(messageResolveService.message(MessageResolverCodes.LANGUAGE_CODE, language))
                .append(": ")
                .append(clientSettingsBean.getLanguage())
                .append(NEXT_LINE);
        sb
                .append(messageResolveService.message(MessageResolverCodes.PRINT_STATISTIC_MODE_CODE, language))
                .append(": ")
                .append(clientSettingsBean.getPrintMacronutrientsMode())
                .append(NEXT_LINE);
        return new SendMessage(
                chatId,
                sb.toString()
        );
    }

    private String buildProfileBiometricDisplayResponse(BiometricBean bean) {
        Language language = ClientSettingsContext.getClientSettingsContextHolder().getBySessionForCurrent().getLanguage();
        StringBuilder sb = new StringBuilder();
        sb
                .append(messageResolveService.message(MessageResolverCodes.AGE_CODE, language))
                .append(": ")
                .append(bean.getAge()).append(NEXT_LINE);
        sb
                .append(messageResolveService.message(MessageResolverCodes.HEIGHT_CODE, language))
                .append(": ")
                .append(bean.getHeight()).append(NEXT_LINE);
        sb
                .append(messageResolveService.message(MessageResolverCodes.WEIGHT_CODE, language))
                .append(": ")
                .append(bean.getWeight()).append(NEXT_LINE);
        sb
                .append(messageResolveService.message(MessageResolverCodes.GENDER_CODE, language))
                .append(": ")
                .append(bean.getGender()).append(NEXT_LINE);
        sb
                .append(messageResolveService.message(MessageResolverCodes.PHYSICAL_ACTIVITY_CODE, language))
                .append(": ")
                .append(bean.getPhysicalActivity()).append(NEXT_LINE);

        return sb.toString();
    }
}
