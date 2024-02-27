package ru.akvine.fitstats.utils;

import com.google.common.base.Strings;

import java.security.SecureRandom;

public final class OneTimePasswordGenerator {
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generate(int length) {
        return Strings.padStart(secureRandom.nextInt((int) Math.pow(10.0D, length) -1) + "", length, '0');
    }
}
