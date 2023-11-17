package ru.akvine.fitstats.validators;

import org.springframework.stereotype.Component;
import ru.akvine.fitstats.enums.VolumeMeasurement;

@Component
public class VolumeMeasurementValidator implements Validator<String> {
    @Override
    public void validate(String measurement) {
        VolumeMeasurement.safeValueOf(measurement);
    }
}
