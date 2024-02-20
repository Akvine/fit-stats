package ru.akvine.fitstats.context;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.services.client.settings.ClientSettingsService;
import ru.akvine.fitstats.services.dto.client.ClientSettingsBean;
import ru.akvine.fitstats.utils.SecurityUtils;

@Component
@RequiredArgsConstructor
public class ClientSettingsContextHolder {
    private final ClientSettingsService clientSettingsService;

    public ClientSettingsBean getByEmail(String email) {
        return clientSettingsService.findBeanByClientEmail(email);
    }

    /**
     * Used for telegram flow
     * @return current user settings
     */
    public ClientSettingsBean getByThreadLocalForCurrent() {
        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        String email = threadLocal.get();
        return clientSettingsService.findBeanByClientEmail(email);
    }

    /**
     * Used for session flow
     * @return current user settings
     */
    public ClientSettingsBean getBySessionForCurrent() {
        String email = SecurityUtils.getCurrentUser().getName();
        return clientSettingsService.findBeanByClientEmail(email);
    }
}
