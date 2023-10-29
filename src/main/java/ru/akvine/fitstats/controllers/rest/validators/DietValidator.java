package ru.akvine.fitstats.controllers.rest.validators;

import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.rest.dto.diet.AddRecordRequest;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.validation.ValidationException;

@Component
public class DietValidator {
    public void verifyAddRecordRequest(AddRecordRequest addRecordRequest) {
        if (addRecordRequest.getDate() == null) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.FIELD_NULL_ERROR,
                    "Date is null. Field name: date");
        }
    }
}
