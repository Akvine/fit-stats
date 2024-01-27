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

    public static final String PRODUCTS = "/products";

    public static final String WEIGHT_RECORD_LIST = "/weight/record/list";
    public static final String WEIGHT_RECORD_CHANGE = "/weight/record/change";
    public static final String WEIGHT_RECORD_DELETE = "/weight/record/delete";
}
