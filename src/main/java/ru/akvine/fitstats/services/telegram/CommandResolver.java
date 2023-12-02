package ru.akvine.fitstats.services.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.enums.CommandEnum;

@Component
@RequiredArgsConstructor
public class CommandResolver {

    private static final String PRODUCT_LIST_MESSAGE = "Список продуктов";
    private static final String DIET_DISPLAY_STATISTIC_MESSAGE = "БЖУ за сегодня";
    private static final String DIET_ADD_RECORD_MESSAGE = "Добавить запись";
    private static final String HELP_MESSAGE = "Помощь";

    public boolean isStartCommand(String command) {
        return CommandEnum.COMMAND_START.getCommandName().equals(command);
    }

    public boolean isProductListCommand(String command) {
        return CommandEnum.COMMAND_PRODUCTS_LIST.getCommandName().equals(command) || command.equals(PRODUCT_LIST_MESSAGE);
    }

    public boolean isDietStatisticDisplayCommand(String command) {
        return CommandEnum.COMMAND_DIET_DISPLAY.getCommandName().equals(command) || command.equals(DIET_DISPLAY_STATISTIC_MESSAGE);
    }

    public boolean isDietAddRecordCommand(String command) {
        return CommandEnum.COMMAND_DIET_ADD_RECORD.getCommandName().equals(command) || command.equals(DIET_ADD_RECORD_MESSAGE);
    }

    public boolean isHelpCommand(String command) {
        return CommandEnum.COMMAND_HELP.getCommandName().equals(command) || command.equals(HELP_MESSAGE);
    }
}
