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
    private static final String BACK_MESSAGE = "Назад";

    private static final String NOTIFICATION_SUBSCRIPTIONS = "Уведомления";
    private static final String NOTIFICATION_SUBSCRIPTION_DIET = "Диета";

    private static final String NOTIFICATION_SUBSCRIPTION_DIET_ADD_MESSAGE = "Добавить подписку на уведомления";
    private static final String NOTIFICATION_SUBSCRIPTION_DIET_LIST_MESSAGE = "Список подписок на уведомления";
    private static final String NOTIFICATION_SUBSCRIPTION_DIET_DELETE_MESSAGE = "Удалить подписку на уведомление";

    public boolean isStartCommand(String command) {
        return CommandEnum.COMMAND_START.getCommandName().equals(command) || command.equals(BACK_MESSAGE);
    }

    public boolean isProductListCommand(String command) {
        return CommandEnum.COMMAND_PRODUCTS_LIST.getCommandName().equals(command) || command.equals(PRODUCT_LIST_MESSAGE);
    }

    public boolean isDietStatisticDisplayCommand(String command) {
        return CommandEnum.COMMAND_DIET_DISPLAY.getCommandName().equals(command) || command.equals(DIET_DISPLAY_STATISTIC_MESSAGE);
    }

    public boolean isNotificationSubscriptionCommand(String command) {
        return command.equals(NOTIFICATION_SUBSCRIPTIONS);
    }

    public boolean isNotificationSubscriptionDietCommand(String command) {
        return command.equals(NOTIFICATION_SUBSCRIPTION_DIET);
    }

    public boolean isDietAddRecordCommand(String command) {
        return CommandEnum.COMMAND_DIET_ADD_RECORD.getCommandName().equals(command) || command.equals(DIET_ADD_RECORD_MESSAGE);
    }

    public boolean isHelpCommand(String command) {
        return CommandEnum.COMMAND_HELP.getCommandName().equals(command) || command.equals(HELP_MESSAGE);
    }

    public boolean isNotificationSubscriptionDietAdd(String command) {
        return CommandEnum.COMMAND_NOTIFICATION_SUBSCRIPTION_DIET_ADD.getCommandName().equals(command) || command.equals(NOTIFICATION_SUBSCRIPTION_DIET_ADD_MESSAGE);
    }

    public boolean isNotificationSubscriptionDietList(String command) {
        return CommandEnum.COMMAND_NOTIFICATION_SUBSCRIPTION_DIET_LIST.getCommandName().equals(command) || command.equals(NOTIFICATION_SUBSCRIPTION_DIET_LIST_MESSAGE);
    }

    public boolean isNotificationSubscriptionDietDelete(String command) {
        return CommandEnum.COMMAND_NOTIFICATION_SUBSCRIPTION_DIET_DELETE.getCommandName().equals(command) || command.equals(NOTIFICATION_SUBSCRIPTION_DIET_DELETE_MESSAGE);
    }

}
