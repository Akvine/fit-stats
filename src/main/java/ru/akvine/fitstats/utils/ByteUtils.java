package ru.akvine.fitstats.utils;

import ru.akvine.fitstats.exceptions.util.ByteConvertException;

import javax.validation.constraints.NotNull;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public final class ByteUtils {
    private static final int BUFFER_SIZE = 1024;

    public static byte[] convertToBytes(@NotNull InputStream inputStream) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            return outputStream.toByteArray();
        } catch (Exception exception) {
            throw new ByteConvertException("Error while converting inputStream to bytes array, ex = " + exception.getMessage());
        }
    }
}
