package ru.akvine.api.rest;

public class TestConstants {
    private TestConstants() {
        throw new IllegalStateException("Calling TestConstants constructor is prohibited!");
    }

    public static final String REQUEST_STATUS_SUCCESS = "SUCCESS";
    public static final String REQUEST_STATUS_FAIL = "FAIL";

    public static final String OTP_NEW_STATE = "NEW";
    public static final String OTP_PASSED_STATE = "OTP_PASSED";

    public static final String OTP = "1111";
    public static final String SESSION_COOKIE_NAME = "SESSION";

    public static final int OTP_NUMBER = 1;
    public static final int OTP_COUNT_LEFT = 2;
    public static final int OTP_NEW_DELAY = 30;
    public static final int OTP_INVALID_ATTEMPTS_LEFT = 3;

    public static final String CLIENT_EMAIL_NOT_EXISTS_1 = "client_email_not_exists1@gmail.com";
    public static final String CLIENT_EMAIL_NOT_EXISTS_2 = "client_email_not_exists2@gmail.com";
    public static final String CLIENT_EMAIL_NOT_EXISTS_3 = "client_email_not_exists3@gmail.com";

    public static final String CLIENT_EMAIL_EXISTS_1 = "client_email_1@mail.com";
    public static final String CLIENT_EMAIL_EXISTS_2 = "client_email_2@mail.com";
    public static final String CLIENT_EMAIL_EXISTS_3 = "client_email_3@mail.com";
    public static final String CLIENT_EMAIL_EXISTS_4 = "client_email_4@mail.com";
    public static final String CLIENT_EMAIL_EXISTS_5 = "client_email_5@mail.com";
    public static final String CLIENT_EMAIL_EXISTS_6 = "client_email_6@mail.com";

    public static final String INVALID_EMAIL = "invalid email";

    public static final String VALID_PASSWORD = "aBcDeF1234";
}
