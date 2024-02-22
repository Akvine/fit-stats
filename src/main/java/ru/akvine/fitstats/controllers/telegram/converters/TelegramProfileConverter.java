package ru.akvine.fitstats.controllers.telegram.converters;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.akvine.fitstats.constants.MessageResolverCodes;
import ru.akvine.fitstats.context.ClientSettingsContext;
import ru.akvine.fitstats.controllers.telegram.dto.profile.TelegramProfileUpdateBiometricRequest;
import ru.akvine.fitstats.enums.Language;
import ru.akvine.fitstats.enums.PhysicalActivity;
import ru.akvine.fitstats.services.MessageResolveService;
import ru.akvine.fitstats.services.dto.client.BiometricBean;
import ru.akvine.fitstats.services.dto.profile.UpdateBiometric;

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

    public SendMessage convertToProfileUpdateBiometricResponse(String chatId, BiometricBean biometricBean) {
        Preconditions.checkNotNull(biometricBean, "biometricBean is null");
        return new SendMessage(
                chatId,
                buildProfileBiometricDisplayResponse(biometricBean)
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
