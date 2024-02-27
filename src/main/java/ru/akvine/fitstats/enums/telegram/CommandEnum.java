package ru.akvine.fitstats.enums.telegram;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CommandEnum {
    COMMAND_START("/start"),
    COMMAND_HELP("/help"),
    COMMAND_BACK("/back"),

    COMMAND_NOTIFICATION_SUBSCRIPTION("/notification/subscription"),
    COMMAND_NOTIFICATION_SUBSCRIPTION_DIET("/notification/subscription/diet"),
    COMMAND_NOTIFICATION_SUBSCRIPTION_DIET_ADD("/notification/subscription/diet/add"),
    COMMAND_NOTIFICATION_SUBSCRIPTION_DIET_LIST("/notification/subscription/diet/list"),
    COMMAND_NOTIFICATION_SUBSCRIPTION_DIET_DELETE("/notification/subscription/diet/delete"),

    COMMAND_PRODUCTS("/products"),
    COMMAND_PRODUCTS_ADD("/products/add"),
    COMMAND_PRODUCTS_LIST("/products/list"),
    COMMAND_PRODUCTS_GET_BY_BARCODE("/products/get/barcode"),

    COMMAND_DIET("/diet"),
    COMMAND_DIET_RECORD_ADD("/diet/record/add"),
    COMMAND_DIET_RECORD_ADD_BY_BARCODE("/diet/record/add/barcode"),
    COMMAND_DIET_RECORD_LIST("/diet/record/list"),
    COMMAND_DIET_RECORD_DELETE("/diet/record/delete"),
    COMMAND_DIET_STATISTIC_DISPLAY("/diet/display"),

    COMMAND_PROFILE("/profile"),
    COMMAND_PROFILE_DISPLAY("/profile/display"),
    COMMAND_PROFILE_UPDATE("/profile/update"),
    COMMAND_PROFILE_SETTINGS("/profile/settings"),
    COMMAND_PROFILE_SETTINGS_UPDATE("/profile/settings/update"),

    COMMAND_STATISTIC("/statistic"),
    COMMAND_STATISTIC_HISTORY("/statistic/history"),
    COMMAND_STATISTIC_INDICATORS("/statistic/indicators");

    private final String commandName;
}
