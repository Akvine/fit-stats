package ru.akvine.fitstats.validators;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.enums.FileType;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.validation.ValidationException;

@Component
public class FileTypeValidator implements Validator<String>  {
    @Override
    public void validate(String fileType) {
        if (StringUtils.isBlank(fileType)) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.FILE_TYPE_BLANK_ERROR,
                    "File type is blank"
            );
        }

        try {
            FileType.valueOf(fileType);
        } catch (IllegalArgumentException exception) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.FILE_TYPE_INVALID_ERROR,
                    "File type = [" + fileType + "] is not supported!"
            );
        }
    }
}
