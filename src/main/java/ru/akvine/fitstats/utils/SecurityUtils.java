package ru.akvine.fitstats.utils;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.akvine.fitstats.config.security.ClientAuthentication;
import ru.akvine.fitstats.exceptions.security.NoSessionException;
import ru.akvine.fitstats.services.dto.client.ClientBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public final class SecurityUtils {
    public static HttpSession getSession(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session == null) {
            throw new NoSessionException();
        }
        return session;
    }

    public static void authenticate(ClientBean client) {
        SecurityContextHolder.getContext().setAuthentication(
                new ClientAuthentication(
                        client.getId(),
                        client.getUuid(),
                        client.getEmail()
                )
        );
    }

    public static ClientAuthentication getCurrentUser() {
        ClientAuthentication user = getCurrentUserOrNull();
        Preconditions.checkNotNull(user, "user is null");
        return user;
    }

    @Nullable
    public static ClientAuthentication getCurrentUserOrNull() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof ClientAuthentication) {
            return (ClientAuthentication) authentication;
        }
        return null;
    }

    public static void doLogout(HttpServletRequest request) {
        if (request == null) {
            return;
        }
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        ClientAuthentication user = SecurityUtils.getCurrentUserOrNull();
        if (user == null) {
            return;
        }
        SecurityContextHolder.clearContext();
    }
}
