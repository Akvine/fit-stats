package ru.akvine.fitstats.services.client.settings;

import lombok.RequiredArgsConstructor;
import ru.akvine.fitstats.services.dto.client.ClientSettingsBean;

@RequiredArgsConstructor
public class DatabaseGetClientSettingsService implements GetClientSettingsService {
    private final ClientSettingsService clientSettingsService;

    @Override
    public ClientSettingsBean getByClientEmail(String email) {
        return clientSettingsService.getByClientEmail(email);
    }
}
