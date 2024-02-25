package ru.akvine.fitstats.validators;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.enums.BarCodeType;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.validation.ValidationException;

@Component
public class BarCodeTypeValidator implements Validator<String> {
    @Override
    public void validate(String type) {
        if (StringUtils.isBlank(type)) {
            throw new ValidationException(CommonErrorCodes.Validation.BarCode.BAR_CODE_TYPE_BLANK_ERROR,
                    "Bar code type is blank. Field name: type");
        }

        try {
            BarCodeType.valueOf(type.toUpperCase());
        } catch (Exception exception) {
            throw new ValidationException(CommonErrorCodes.Validation.BarCode.BAR_CODE_TYPE_INVALID_ERROR,
                    "Bar code type is blank. Field name: type");
        }
    }
}
