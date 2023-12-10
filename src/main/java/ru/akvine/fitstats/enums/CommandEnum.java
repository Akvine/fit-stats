package ru.akvine.fitstats.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum  CommandEnum {
    COMMAND_START("/start"),
    COMMAND_HELP("/help"),
    COMMAND_PRODUCTS_LIST("/products/list"),
    COMMAND_DIET_DISPLAY("/diet/display"),
    COMMAND_DIET_ADD_RECORD("/diet/record/add"),

    COMMAND_NOTIFICATION_SUBSCRIPTION_DIET_ADD("/notification/subscription/diet/add"),
    COMMAND_NOTIFICATION_SUBSCRIPTION_DIET_LIST("/notification/subscription/diet/list"),
    COMMAND_NOTIFICATION_SUBSCRIPTION_DIET_DELETE("/notification/subscription/diet/delete");

    private final String commandName;
}
