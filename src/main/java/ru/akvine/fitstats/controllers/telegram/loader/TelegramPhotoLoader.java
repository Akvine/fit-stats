package ru.akvine.fitstats.controllers.telegram.loader;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import ru.akvine.fitstats.controllers.telegram.bot.TelegramLongPoolingBot;
import ru.akvine.fitstats.exceptions.telegram.TelegramLoadPhotoException;
import ru.akvine.fitstats.services.properties.PropertyService;
import ru.akvine.fitstats.utils.ByteUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class TelegramPhotoLoader {

    private static final int BUFFER_SIZE = 1024;

    @Autowired
    // TODO : циклическая зависимость (TOO BAD)
    public void setTelegramBot(@Lazy TelegramLongPoolingBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @Autowired
    public void setPropertyService(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    private TelegramLongPoolingBot telegramBot;
    private PropertyService propertyService;

    @Value("telegram.bot.token")
    private String botTokenPropertyName;

    public byte[] load(String fileId) {
        Preconditions.checkNotNull(fileId, "fileId is null");
        String botToken = propertyService.get(botTokenPropertyName);

        try {
            GetFile getFileRequest = new GetFile();
            getFileRequest.setFileId(fileId);
            org.telegram.telegrambots.meta.api.objects.File file = telegramBot.execute(getFileRequest);

            String fileUrl = file.getFileUrl(botToken);

            URL url = new URL(fileUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            return ByteUtils.convertToBytes(connection.getInputStream());
        } catch (Exception exception) {
            throw new TelegramLoadPhotoException("Error while loading photo from Telegram by API, ex = " + exception.getMessage());
        }
    }
}
