package ru.akvine.fitstats.constants;

public class DBLockPrefixesConstants {
    private DBLockPrefixesConstants() {
        throw new IllegalStateException("Call DBLockPrefixesConstants constructor is prohibited!");
    }

    public static final String ACCESS_RESTORE_PREFIX = "ACCESS_RESTORE_";
    public static final String AUTH_PREFIX = "AUTH_";
    public static final String REG_PREFIX = "REG_";
    public static final String PROFILE_CHANGE_EMAIL_PREFIX = "PROFILE_CHANGE_EMAIL_";
    public static final String PROFILE_CHANGE_PASSWORD_PREFIX = "PROFILE_CHANGE_PASSWORD_";
    public static final String PROFILE_DELETE_PREFIX = "PROFILE_DELETE_";
}
