package ru.akvine.fitstats.services.processors.format;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.enums.ConverterType;
import ru.akvine.fitstats.exceptions.util.CsvException;
import ru.akvine.fitstats.services.properties.PropertyParseService;
import ru.akvine.fitstats.utils.ReflectionUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CsvConverter implements Converter {
    private final PropertyParseService propertyParseService;

    @Value("csv.file.converter.separator")
    private String csvDelimiter;

    private final static int START_INDEX = 0;
    private final static String ROW_NUMBER_HEADER = "rowNumber";

    @Override
    public <T> byte[] convert(List<T> records, Class<T> clazz) {
        Preconditions.checkNotNull(records, "records is null");
        Preconditions.checkNotNull(clazz, "class is null");

        CSVFormat format = CSVFormat.DEFAULT.builder().setDelimiter(propertyParseService.parseChar(csvDelimiter)).build();
        ReflectionUtils.getFieldNames(clazz);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format)) {
            List<String> headers = ReflectionUtils.getFieldNames(clazz);
            headers.add(START_INDEX, ROW_NUMBER_HEADER);
            csvPrinter.printRecord(headers);
            int rowNumber = 1;
            for (T dietRecordCsv : records) {
                List<String> data = ReflectionUtils.getFieldsValues(dietRecordCsv);
                data.add(START_INDEX, String.valueOf(rowNumber));
                csvPrinter.printRecord(data);
                rowNumber++;
            }

            csvPrinter.flush();
            return out.toByteArray();
        } catch (IOException e) {
            throw new CsvException("Fail convert records to csv: " + e.getMessage());
        }
    }

    @Override
    public ConverterType getType() {
        return ConverterType.CSV;
    }
}
