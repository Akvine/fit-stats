package ru.akvine.fitstats.constants;

public class MessageResolverCodes {
    private MessageResolverCodes() {
        throw new IllegalStateException("Calling MessageResolverCodes constructor is prohibited!");
    }

    public final static String CALORIES_CODE = "calories.code";
    public final static String PROTEINS_CODE = "proteins.code";
    public final static String FATS_CODE = "fats.code";
    public final static String CARBOHYDRATES_CODE = "carbohydrates.code";
    public final static String ALCOHOL_CODE = "alcohol.code";
    public final static String VOLUME_CODE = "volume.code";

    public final static String REMAINED_CODE = "remained.code";
    public final static String CONSUMED_CODE = "consumed.code";
    public final static String MAXIMUM_CODE = "maximum.code";

    public final static String DIET_RECORD_ADD_SUCCESSFUL_CODE = "diet.record.add.successful.code";
    public final static String DIET_RECORD_DELETE_SUCCESSFUL_CODE = "diet.record.delete.successful.code";
    public final static String DIET_RECORD_LIST_CODE = "diet.record.list.code";

    public final static String DIET_NOTIFICATION_SUBSCRIPTION_ACTIVE_LIST_CODE =
            "diet.notification.subscription.active.list.code";

    public static final String PRODUCT_UUID_CODE = "product.uuid.code";
    public static final String PRODUCT_NAME_CODE = "product.name.code";

    public static final String MEASUREMENT_TYPE_CODE = "measurement.type.code";

    public static final String PRODUCT_NOT_FOUND_CODE = "product.notFound.code";
    public static final String PRODUCT_LIST_CODE = "product.list.code";
    public static final String PRODUCT_ADDED_SUCCESSFUL_CODE = "product.add.successful.code";

    public static final String PRODUCER_NAME_CODE = "producer.name.code";

    public static final String DIET_NOTIFICATION_SUBSCRIPTION_PROTEINS_LIMIT_EXCEED_CODE = "diet.notification.subscription.proteins.limit.exceed.code";
    public static final String DIET_NOTIFICATION_SUBSCRIPTION_FATS_LIMIT_EXCEED_CODE = "diet.notification.subscription.fats.limit.exceed.code";
    public static final String DIET_NOTIFICATION_SUBSCRIPTION_CARBOHYDRATES_LIMIT_EXCEED_CODE = "diet.notification.subscription.carbohydrates.limit.exceed.code";
    public static final String DIET_NOTIFICATION_SUBSCRIPTION_CALORIES_LIMIT_EXCEED_CODE = "diet.notification.subscription.calories.limit.exceed.code";
    public static final String DIET_NOTIFICATION_SUBSCRIPTION_ALL_LIMIT_EXCEED_CODE = "diet.notification.subscription.all.limit.exceed.code";

    public static final String AGE_CODE = "age.code";
    public static final String HEIGHT_CODE = "height.code";
    public static final String WEIGHT_CODE = "weight.code";
    public static final String GENDER_CODE = "gender.code";
    public static final String PHYSICAL_ACTIVITY_CODE = "physical.activity.code";

    public static final String DURATION_CODE = "duration.code";
    public static final String MACRONUTRIENT_CODE = "macronutrient.code";
    public static final String AVERAGE_CODE = "average.code";
    public static final String MEDIAN_CODE = "median.code";

    public static final String PROTEINS_PERCENT_DIET_CODE = "proteins.percent.diet.code";
    public static final String FATS_PERCENT_DIET_CODE = "fats.percent.diet.code";
    public static final String CARBOHYDRATES_PERCENT_DIET_CODE = "carbohydrates.percent.diet.code";
    public static final String ALCOHOL_PERCENT_DIET_CODE = "alcohol.percent.diet.code";

    public static final String TELEGRAM_SUBSCRIPTION_ADDED_CODE = "telegram.subscription.added.code";
    public static final String TELEGRAM_SUBSCRIPTION_DELETED_CODE = "telegram.subscription.deleted.code";

    public static final String TELEGRAM_WELCOME_MESSAGE_CODE = "telegram.welcome.message.code";
    public static final String TELEGRAM_UNKNOWN_COMMAND_MESSAGE_CODE = "telegram.unknown.command.message.code";
    public static final String TELEGRAM_LIST_AVAILABLE_COMMANDS_CODE = "telegram.list.available.commands.code";
    public static final String TELEGRAM_CHOOSE_ACTION_CODE = "telegram.choose.action.code";
    public static final String TELEGRAM_INVALID_AUTH_CODE = "telegram.invalid.auth.code";
    public static final String TELEGRAM_HELP_CODE = "telegram.help.code";
    public static final String TELEGRAM_BACK_CODE = "telegram.back.code";
    public static final String TELEGRAM_START_CODE = "telegram.start.code";
    public static final String TELEGRAM_NOTIFICATION_SUBSCRIPTION_CODE = "telegram.notification.subscription.code";
    public static final String TELEGRAM_NOTIFICATION_SUBSCRIPTION_DIET_CODE = "telegram.notification.subscription.diet.code";
    public static final String TELEGRAM_NOTIFICATION_SUBSCRIPTION_DIET_ADD_CODE = "telegram.notification.subscription.diet.add.code";
    public static final String TELEGRAM_NOTIFICATION_SUBSCRIPTION_DIET_LIST_CODE = "telegram.notification.subscription.diet.list.code";
    public static final String TELEGRAM_NOTIFICATION_SUBSCRIPTION_DIET_DELETE_CODE = "telegram.notification.subscription.diet.delete.code";
    public static final String TELEGRAM_PRODUCT_CODE = "telegram.product.code";
    public static final String TELEGRAM_PRODUCT_ADD_CODE = "telegram.product.add.code";
    public static final String TELEGRAM_PRODUCT_LIST_CODE = "telegram.product.list.code";
    public static final String TELEGRAM_DIET_CODE = "telegram.diet.code";
    public static final String TELEGRAM_DIET_ADD_CODE = "telegram.diet.add.code";
    public static final String TELEGRAM_DIET_LIST_CODE = "telegram.diet.list.code";
    public static final String TELEGRAM_DIET_DELETE_CODE = "telegram.diet.delete.code";
    public static final String TELEGRAM_DIET_STATISTIC_DISPLAY_CODE = "telegram.diet.statistic.display.code";
    public static final String TELEGRAM_PROFILE_CODE = "telegram.profile.code";
    public static final String TELEGRAM_PROFILE_BIOMETRIC_DISPLAY_CODE = "telegram.profile.biometric.display.code";
    public static final String TELEGRAM_PROFILE_UPDATE_CODE = "telegram.profile.update.code";
    public static final String TELEGRAM_STATISTIC_CODE = "telegram.statistic.code";
    public static final String TELEGRAM_STATISTIC_HISTORY_CODE = "telegram.statistic.history.code";
    public static final String TELEGRAM_STATISTIC_INDICATORS_CODE = "telegram.statistic.indicators.code";
    public static final String TELEGRAM_STATISTIC_HISTORY_INPUT_WAITING_CODE = "telegram.statistic.history.input.waiting.code";
    public static final String TELEGRAM_CHOOSE_SUBSCRIPTION_TYPE_CODE = "telegram.choose.subscription.type.code";
    public static final String TELEGRAM_INPUT_SUBSCRIPTION_TYPE_FOR_DELETE_CODE = "telegram.choose.subscription.type.for.delete.code";
    public static final String TELEGRAM_INPUT_SUBSCRIPTION_TYPE_FOR_ADD_CODE = "telegram.choose.subscription.type.for.add.code";
    public static final String TELEGRAM_PROFILE_BIOMETRIC_INPUT_NEW_DATA_CODE = "telegram.profile.biometric.input.new.data.code";
    public static final String TELEGRAM_PRODUCT_INPUT_FILTER_CODE = "telegram.product.input.filter.code";
    public static final String TELEGRAM_DIET_RECORD_DELETE_CODE = "telegram.diet.record.delete.code";
    public static final String TELEGRAM_DIET_RECORD_ADD_CODE = "telegram.diet.record.add.code";
    public static final String TELEGRAM_PRODUCT_INPUT_ADD_CODE = "telegram.product.add.input.code";

    public static final String TELEGRAM_GENERAL_ERROR_CODE = "telegram.general.error.code";
    public static final String TELEGRAM_PRODUCT_NOT_FOUND_ERROR_CODE = "telegram.product.notFound.error.code";
    public static final String TELEGRAM_MACRONUTRIENT_PROTEINS_INPUT_ERROR_CODE = "telegram.macronutrient.proteins.input.error.code";
    public static final String TELEGRAM_MACRONUTRIENT_FATS_INPUT_ERROR_CODE = "telegram.macronutrient.fats.input.error.code";
    public static final String TELEGRAM_MACRONUTRIENT_CARBOHYDRATES_INPUT_ERROR_CODE = "telegram.macronutrient.carbohydrates.input.error.code";
    public static final String TELEGRAM_MACRONUTRIENT_VOL_INPUT_ERROR_CODE = "telegram.macronutrient.vol.input.error.code";
    public static final String TELEGRAM_PRODUCT_UUID_NOT_UNIQUE_ERROR_CODE = "telegram.product.uuid.notUnique.error.code";
    public static final String TELEGRAM_DIET_UUID_NOT_UNIQUE_ERROR_CODE = "telegram.diet.uuid.notUnique.error.code";
    public static final String TELEGRAM_BLOCKED_CREDENTIALS_ERROR_CODE = "telegram.blocked.credentials.error.code";
}
