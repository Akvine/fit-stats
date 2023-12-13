package ru.akvine.fitstats.config.security.ssl;

import org.jetbrains.annotations.Nullable;

import javax.net.ssl.HostnameVerifier;

public interface HostnameVerifierProvider {
    @Nullable HostnameVerifier getHostnameVerifier();
}
