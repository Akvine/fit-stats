package ru.akvine.fitstats.controllers.telegram.converters;

import com.google.common.base.Preconditions;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.akvine.fitstats.controllers.telegram.dto.profile.TelegramProfileUpdateBiometricRequest;
import ru.akvine.fitstats.enums.PhysicalActivity;
import ru.akvine.fitstats.services.dto.client.BiometricBean;
import ru.akvine.fitstats.services.dto.profile.UpdateBiometric;

@Component
public class TelegramProfileConverter {
    private final static String NEXT_LINE = "\n";

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
        StringBuilder sb = new StringBuilder();
        sb.append("Возраст: ").append(bean.getAge()).append(NEXT_LINE);
        sb.append("Рост: ").append(bean.getHeight()).append(NEXT_LINE);
        sb.append("Вес: ").append(bean.getWeight()).append(NEXT_LINE);
        sb.append("Пол: ").append(bean.getGender()).append(NEXT_LINE);
        sb.append("Физическая активность: ").append(bean.getPhysicalActivity()).append(NEXT_LINE);

        return sb.toString();
    }
}
