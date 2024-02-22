package ru.akvine.fitstats.services.client.settings;

import lombok.RequiredArgsConstructor;
import ru.akvine.fitstats.cache.ClientSettingsCache;
import ru.akvine.fitstats.exceptions.cache.ClientSettingsCacheException;
import ru.akvine.fitstats.services.dto.client.ClientSettingsBean;


@RequiredArgsConstructor
public class CachableGetClientSettingsService implements GetClientSettingsService {
    private final ClientSettingsCache cache;

    @Override
    public ClientSettingsBean getByClientEmail(String email) {
        try {
            return cache.getClientSettings().get(email);
        } catch (Exception exception) {
            throw new ClientSettingsCacheException("Error while getting client settings value from cache by key = " + email);
        }
    }
}
