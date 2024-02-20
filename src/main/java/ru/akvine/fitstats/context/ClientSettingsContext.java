package ru.akvine.fitstats.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientSettingsContext {
    private static ClientSettingsContextHolder clientSettingsContextHolder;

    @Autowired
    public void setClientSettingsContextHolder(ClientSettingsContextHolder holder) {
        ClientSettingsContext.clientSettingsContextHolder = holder;
    }

    public static ClientSettingsContextHolder getClientSettingsContextHolder() {
        return clientSettingsContextHolder;
    }
}
