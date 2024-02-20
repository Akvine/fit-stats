package ru.akvine.fitstats.validators.file;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.akvine.fitstats.enums.FileType;
import ru.akvine.fitstats.services.properties.PropertyParseService;

@Component
@RequiredArgsConstructor
public class XlsxValidator extends FileValidator {
    private final PropertyParseService propertyParseService;

    @Value("xlsx.file.converter.max-size.bytes")
    private String fileMaxSizeBytes;
    @Value("xlsx.file.converter.content-available.types")
    private String availableTypes;

    @Override
    public void validate(MultipartFile file) {
        validate(file,
                propertyParseService.parseLong(fileMaxSizeBytes),
                propertyParseService.parseToList(availableTypes));
    }

    @Override
    public FileType getType() {
        return FileType.XLSX;
    }
}
