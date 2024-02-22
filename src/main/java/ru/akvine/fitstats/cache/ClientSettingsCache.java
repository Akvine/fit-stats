package ru.akvine.fitstats.cache;

import com.google.common.cache.LoadingCache;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.services.dto.client.ClientSettingsBean;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class ClientSettingsCache {
    private LoadingCache<String, ClientSettingsBean> clientSettings;
}
