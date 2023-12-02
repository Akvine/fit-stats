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
    COMMAND_DIET_ADD_RECORD("/diet/record/add");

    private final String commandName;
}
