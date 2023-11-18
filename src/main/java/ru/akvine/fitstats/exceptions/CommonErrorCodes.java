package ru.akvine.fitstats.exceptions;

public class CommonErrorCodes {
    private CommonErrorCodes() {throw new IllegalStateException("CommonErrorCodes can't be called!");}

    public final static String GENERAL_ERROR = "general.error";
    public final static String NO_SESSION = "no.session.error";

    public interface Client {
        String CLIENT_ALREADY_EXISTS_ERROR = "client.already.exists.error";
        String CLIENT_NOT_FOUND_ERROR = "client.notFound.error";
    }

    public interface Product {
        String PRODUCT_NOT_FOUND_ERROR = "product.notFound.error";
    }

    public interface Diet {
        String DIET_SETTING_NOT_FOUND_ERROR = "diet.setting.notFound.error";
    }

    public interface Security {
        String AUTHENTICATE_ERROR = "authenticate.error";
    }

    public interface Validation {
        String FIELD_NOT_PRESENTED_ERROR = "field.not.presented.error";

        interface Admin {
            String UUID_VALUE_BLANK_ERROR = "uuid.value.blank.error";
            String MACRONUTRIENT_INVALID_ERROR = "macronutrient.invalid.error";
        }

        interface Product {
            String MACRONUTRIENT_VALUE_INVALID_ERROR = "macronutrient.value.invalid.error";
            String TITLE_BLANK_ERROR = "title.blank.error";
            String PRODUCER_BLANK_ERROR = "producer.blank.error";
        }

        interface Profile {
            String PROFILE_RECORDS_DOWNLOAD_ERROR = "profile.records.download.error";
        }

        interface Statistic {
            String INDICATOR_NOT_SUPPORTED_ERROR = "indicator.notSupported.error";
            String MACRONUTRIENT_NOT_SUPPORTED_ERROR = "macronutrient.notSupported.error";
            String DURATION_BLANK_ERROR = "duration.blank.error";
            String DURATION_INVALID_ERROR = "duration.invalid.error";
            String DATE_RANGE_VALUES_EMPTY = "dateRange.fields.empty.error";
            String ILLEGAL_DATE_RANGE_STATE_ERROR = "illegal.dateRange.fields.state.error";
            String START_DATE_AFTER_END_DATE_ERROR = "startDate.after.endDate.error";
            String ROUND_ACCURACY_INVALID_ERROR = "round.accuracy.invalid.error";
            String MODE_COUNT_INVALID_ERROR = "mode.count.invalid.error";
        }

        interface Client {
            String EMAIL_BLANK_ERROR = "email.blank.error";
            String EMAIL_INVALID_ERROR = "email.invalid.error";
        }

        interface Biometric {
            String AGE_INVALID_ERROR = "age.invalid.error";
            String WEIGHT_INVALID_ERROR = "weight.invalid.error";
            String HEIGHT_INVALID_ERROR = "height.invalid.error";
            String WEIGHT_MEASUREMENT_BLANK_ERROR = "weight.measurement.blank.error";
            String HEIGHT_MEASUREMENT_BLANK_ERROR = "height.measurement.blank.error";
            String WEIGHT_MEASUREMENT_INVALID_ERROR = "weight.measurement.invalid.error";
            String HEIGHT_MEASUREMENT_INVALID_ERROR = "height.measurement.invalid.error";
            String GENDER_BLANK_ERROR = "gender.blank.error";
            String DIET_BLANK_ERROR = "diet.blank.error";
            String GENDER_INVALID_ERROR = "gender.invalid.error";
            String DIET_INVALID_ERROR = "diet.invalid.error";
            String FIELD_NUMBER_INVALID = "field.number.error";
            String PHYSICAL_ACTIVITY_BLANK_ERROR = "physical.activity.blank.error";
            String PHYSICAL_ACTIVITY_INVALID_ERROR = "physical.activity.invalid.error";
        }
    }
}
