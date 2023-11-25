package ru.akvine.fitstats.controllers.rest.converter.parser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.akvine.fitstats.enums.ConverterType;
import ru.akvine.fitstats.exceptions.profile.CsvParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class CsvParser implements Parser {
    @Value("${csv.file.converter.separator}")
    private char separator;
    @Value("${csv.file.converter.skip.lines.count}")
    private int skipLinesCount;

    @Override
    public List<?> parse(MultipartFile file, Class clazz) {
        List<?> csvRows;
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            CsvToBean<?> csvToBean = new CsvToBeanBuilder<>(reader)
                    .withType(clazz)
                    .withSkipLines(skipLinesCount)
                    .withSeparator(separator)
                    .withIgnoreQuotations(true)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            csvRows = csvToBean.parse();
        } catch (IllegalStateException | IOException exception) {
            throw new CsvParseException("CSV parse exception has occurred. Message=" + exception.getMessage());
        }
        return csvRows;
    }

    @Override
    public ConverterType getType() {
        return ConverterType.CSV;
    }
}
