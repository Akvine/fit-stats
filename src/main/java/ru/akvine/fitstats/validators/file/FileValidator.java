package ru.akvine.fitstats.validators.file;

import org.springframework.web.multipart.MultipartFile;
import ru.akvine.fitstats.enums.FileType;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.validation.ValidationException;

import java.util.List;

public abstract class FileValidator {
    public void validate(MultipartFile file, Long fileMaxSizeBytes, List<String> fileAvailableContentTypes) {
        if (file.getSize() == 0L) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.FILE_SIZE_INVALID_ERROR,
                    "File size can't be equals 0");
        }

        if (file.getSize() > fileMaxSizeBytes) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.FILE_SIZE_INVALID_ERROR,
                    "File size can't be greater than bytes limit = [" + fileMaxSizeBytes + "]");
        }

        if (!fileAvailableContentTypes.contains(file.getContentType())) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.FILE_CONTENT_TYPE_INVALID_ERROR,
                    "File content type = [" + file.getContentType() + "] is not supported!");
        }
    }

    public abstract void validate(MultipartFile file);

    public abstract FileType getType();
}
