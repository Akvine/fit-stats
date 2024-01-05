package ru.akvine.fitstats.controllers.rest.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.controllers.rest.dto.diet.ChangeDietRequest;
import ru.akvine.fitstats.validators.DietBaseValidator;

@Component
@RequiredArgsConstructor
public class DietValidator {
    private final DietBaseValidator dietBaseValidator;

    public void verifyUpdateDietRequest(ChangeDietRequest request) {
        dietBaseValidator.validate(request.getDietType());
    }
}
