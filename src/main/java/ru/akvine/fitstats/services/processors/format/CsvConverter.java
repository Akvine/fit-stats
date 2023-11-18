package ru.akvine.fitstats.services.processors.format;

import com.google.common.base.Preconditions;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.enums.ConverterType;
import ru.akvine.fitstats.exceptions.util.CsvException;
import ru.akvine.fitstats.utils.ReflectionUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Component
public class CsvConverter implements Converter {
    @Value("${csv.file.converter.separator}")
    private char csvDelimiter;

    @Override
    public <T> byte[] convert(List<T> records, Class<T> clazz) {
        Preconditions.checkNotNull(records, "records is null");
        Preconditions.checkNotNull(clazz, "class is null");

        CSVFormat format = CSVFormat.DEFAULT.builder().setDelimiter(csvDelimiter).build();
        ReflectionUtils.getFieldNames(clazz);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format)) {
            List<String> headers = ReflectionUtils.getFieldNames(clazz);
            csvPrinter.printRecord(headers);
            for (T dietRecordCsv : records) {
                List<String> data = ReflectionUtils.getFieldsValues(dietRecordCsv);
                csvPrinter.printRecord(data);
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
