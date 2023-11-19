package ru.akvine.fitstats.validators;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.enums.NotificationProviderType;

@Component
public class NotificationValidator implements Validator<String> {
    @Override
    public void validate(String type) {
        if (StringUtils.isBlank(type)) {
            throw new IllegalStateException("Notification provider type is blank");
        }
        try {
            NotificationProviderType.valueOf(type);
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException("Notification provider with type = [" + type + "] not supported!");
        }
    }
}
