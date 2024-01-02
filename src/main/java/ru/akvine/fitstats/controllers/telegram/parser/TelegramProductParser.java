package ru.akvine.fitstats.controllers.telegram.parser;

import com.google.common.base.Preconditions;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.telegram.dto.product.TelegramProductAddRequest;
import ru.akvine.fitstats.exceptions.telegram.parse.TelegramFatsParseException;

@Component
public class TelegramProductParser {
    private static final String COMMA = ",";

    public TelegramProductAddRequest parseTelegramProductAddRequest(String text) {
        Preconditions.checkNotNull(text, "text is null");

        String[] parts = text.trim().split(COMMA);

        String title = parts[0].trim();
        String producer = parts[1].trim();

        double fats, proteins, carbohydrates, vol;

        try {
            fats = Double.parseDouble(parts[2]);
        } catch (NumberFormatException exception) {
            throw new TelegramFatsParseException("Can't parse fats!");
        }

        try {
            proteins = Double.parseDouble(parts[3]);
        } catch (NumberFormatException exception) {
            throw new TelegramFatsParseException("Can't parse proteins!");
        }

        try {
            carbohydrates = Double.parseDouble(parts[4]);
        } catch (NumberFormatException exception) {
            throw new TelegramFatsParseException("Can't parse carbohydrates!");
        }

        try {
            vol = Double.parseDouble(parts[5]);
        } catch (NumberFormatException exception) {
            throw new TelegramFatsParseException("Can't parse vol!");
        }

        String volumeMeasurement = parts[6].trim();
        return new TelegramProductAddRequest()
                .setTitle(title)
                .setProducer(producer)
                .setFats(fats)
                .setProteins(proteins)
                .setCarbohydrates(carbohydrates)
                .setVol(vol)
                .setVolumeMeasurement(volumeMeasurement);
    }
}
