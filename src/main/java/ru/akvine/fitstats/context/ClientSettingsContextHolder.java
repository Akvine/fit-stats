package ru.akvine.fitstats.context;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.services.client.settings.GetClientSettingsService;
import ru.akvine.fitstats.services.dto.client.ClientSettingsBean;
import ru.akvine.fitstats.utils.SecurityUtils;

@Component
@RequiredArgsConstructor
public class ClientSettingsContextHolder {
    private final GetClientSettingsService getClientSettingsService;

    public ClientSettingsBean getByEmail(String email) {
        return getClientSettingsService.getByClientEmail(email);
    }


    public ClientSettingsBean getBySessionForCurrent() {
        String email = TelegramAuthContext.get();
        if (email == null) {
            email = SecurityUtils.getCurrentUser().getName();
        }
        return getClientSettingsService.getByClientEmail(email);
    }
}
