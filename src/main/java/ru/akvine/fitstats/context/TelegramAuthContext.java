package ru.akvine.fitstats.context;

import org.jetbrains.annotations.Nullable;

public class TelegramAuthContext {
    private static final ThreadLocal<String> AUTH_EMAIL_CONTEXT = new InheritableThreadLocal<>();

    @Nullable
    public static String get() {
        return AUTH_EMAIL_CONTEXT.get();
    }

    public static void set(String email) {
        AUTH_EMAIL_CONTEXT.set(email);
    }
}
