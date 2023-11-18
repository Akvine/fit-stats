package ru.akvine.fitstats.validators.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.akvine.fitstats.enums.FileType;

import java.util.List;

@Component
public class CsvValidator extends FileValidator {
    @Value("${csv.file.converter.max-size.bytes}")
    private long fileMaxSizeBytes;
    @Value("${csv.file.converter.content-available.types}")
    private List<String> availableTypes;

    @Override
    public void validate(MultipartFile file) {
        validate(file, fileMaxSizeBytes, availableTypes);
    }

    @Override
    public FileType getType() {
        return FileType.CSV;
    }
}
