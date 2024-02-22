package ru.akvine.fitstats.services.client.settings;

import ru.akvine.fitstats.services.dto.client.ClientSettingsBean;

public interface GetClientSettingsService {
    ClientSettingsBean getByClientEmail(String key);
}
