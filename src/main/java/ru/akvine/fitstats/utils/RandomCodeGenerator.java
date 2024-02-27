package ru.akvine.fitstats.utils;

import org.apache.commons.lang3.RandomStringUtils;

public final class RandomCodeGenerator {
    public static String generateNewRandomCode(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }
}
