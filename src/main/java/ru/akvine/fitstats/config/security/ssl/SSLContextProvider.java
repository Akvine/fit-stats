package ru.akvine.fitstats.config.security.ssl;

import javax.net.ssl.SSLContext;

public interface SSLContextProvider {
    SSLContext getSslContext();
}
