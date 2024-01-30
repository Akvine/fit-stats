package ru.akvine.fitstats.api.config;

public class RestMethods {
    public RestMethods() {throw new IllegalStateException("Can't call RestMethods constructor");}

    public static final String REGISTRATION_START = "/registration/start";
    public static final String REGISTRATION_CHECK = "/registration/check";
    public static final String REGISTRATION_NEWOTP = "/registration/newotp";
    public static final String REGISTRATION_FINISH = "/registration/finish";

    public static final String AUTH_START = "/auth/start";
    public static final String AUTH_NEW_OTP = "/auth/newotp";
    public static final String AUTH_FINISH = "/auth/finish";

    public static final String ACCESS_RESTORE_START = "/access/restore/start";
    public static final String ACCESS_RESTORE_NEWOTP = "/access/restore/newotp";
    public static final String ACCESS_RESTORE_CHECK = "/access/restore/check";
    public static final String ACCESS_RESTORE_FINISH = "/access/restore/finish";

    public static final String PRODUCTS = "/products";

    public static final String TELEGRAM_AUTH_CODE_GENERATE = "/telegram/auth/code/generate";
    public static final String TELEGRAM_AUTH_CODE_GET = "/telegram/auth/code/get";

    public static final String DIET_RECORDS_ADD = "/diet/records/add";
    public static final String DIET_RECORDS_LIST = "/diet/records/list";
    public static final String DIET_RECORDS_DELETE = "/diet/records/delete";
    public static final String DIET_DISPLAY = "/diet/display";
    public static final String DIET_CHANGE = "/diet/change";

    public static final String WEIGHT_RECORD_LIST = "/weight/record/list";
    public static final String WEIGHT_RECORD_CHANGE = "/weight/record/change";
    public static final String WEIGHT_RECORD_DELETE = "/weight/record/delete";
}
