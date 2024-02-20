package ru.akvine.fitstats.managers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.fitstats.enums.FileType;
import ru.akvine.fitstats.validators.file.FileValidator;

import java.util.Map;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class FileValidatorsManager {
    private Map<FileType, FileValidator> availableFileValidators;
}
