package ru.akvine.fitstats.controllers.rest.validators;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.rest.dto.diet.DeleteRecordsRequest;
import ru.akvine.fitstats.exceptions.CommonErrorCodes;
import ru.akvine.fitstats.exceptions.validation.ValidationException;

@Component
public class DietValidator {
    @Value("${db.get.entities.limit}")
    private int dbGetEntitiesLimit;

    public void verifyDeleteRecordsRequest(DeleteRecordsRequest request) {
        Preconditions.checkNotNull(request, "deleteRecordsRequest is null");
        if (request.getRecordsUuids().size() > dbGetEntitiesLimit) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.ELEMENTS_COUNT_TOO_LARGE_ERROR,
                    "Records uuid for deleting is too large. Max count = [" + dbGetEntitiesLimit + "]"
            );
        }
    }
}
