package ru.akvine.fitstats.exceptions.security;

public class OtpExpiredException extends RuntimeException {
    private final int otpCountLeft;

    public OtpExpiredException() {
        this.otpCountLeft = 0;
    }

    public OtpExpiredException(int otpCountLeft) {
        this.otpCountLeft = otpCountLeft;
    }

    public int  getOtpCountLeft() {
        return otpCountLeft;
    }
}
