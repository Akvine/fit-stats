package ru.akvine.fitstats.services.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.enums.CommandEnum;

import static ru.akvine.fitstats.services.telegram.CommandResolver.Actions.*;
import static ru.akvine.fitstats.services.telegram.CommandResolver.Buttons.*;

@Component
@RequiredArgsConstructor
public class CommandResolver {
    public interface Buttons {
        String BASE_HELP_BUTTON = "Помощь";
        String BACK_BUTTON = "Назад";

        String NOTIFICATION_SUBSCRIPTION_BUTTON = "Уведомления";
        String NOTIFICATION_SUBSCRIPTION_DIET_BUTTON = "Диета";
        String NOTIFICATION_SUBSCRIPTION_STATISTIC_BUTTON = "Статистика";

        String PRODUCTS_BUTTON = "Продукты";

        String DIET_BUTTON = "Диетический дневник";

        String PROFILE_BUTTON = "Профиль";
    }

    public interface Actions {
        String NOTIFICATION_SUBSCRIPTION_DIET_ADD_ACTION = "Добавить подписку на уведомления";
        String NOTIFICATION_SUBSCRIPTION_DIET_LIST_ACTION = "Список подписок на уведомления";
        String NOTIFICATION_SUBSCRIPTION_DIET_DELETE_ACTION = "Удалить подписку на уведомление";

        String PRODUCTS_ADD_ACTION = "Добавить продукт";
        String PRODUCTS_LIST_ACTION = "Список продуктов";

        String DIET_ADD_ACTION = "Добавить запись";
        String DIET_LIST_ACTION = "Список записей";
        String DIET_DELETE_ACTION = "Удалить запись";
        String DIET_STATISTIC_DISPLAY_ACTION = "КБЖУ за сегодня";

        String PROFILE_DISPLAY_ACTION = "Биометрические показатели";
        String PROFILE_UPDATE_ACTION = "Обновить показатели";

        String STATISTIC_HISTORY_ACTION = "История";
        String STATISTIC_ADDITIONAL_ACTION = "Основные показатели";
    }


    public boolean isStartCommand(String command) {
        return CommandEnum.COMMAND_START.getCommandName().equals(command);
    }

    public boolean isBackCommand(String command) {
        return command.equals(BACK_BUTTON);
    }

    public boolean isHelpCommand(String command) {
        return CommandEnum.COMMAND_HELP.getCommandName().equals(command) || command.equals(BASE_HELP_BUTTON);
    }

    public boolean isNotificationSubscriptionButton(String command) {
        return command.equals(NOTIFICATION_SUBSCRIPTION_BUTTON);
    }

    public boolean isNotificationSubscriptionDietButton(String command) {
        return command.equals(NOTIFICATION_SUBSCRIPTION_DIET_BUTTON);
    }

    public boolean isNotificationSubscriptionDietAdd(String command) {
        return CommandEnum.COMMAND_NOTIFICATION_SUBSCRIPTION_DIET_ADD.getCommandName().equals(command) || command.equals(NOTIFICATION_SUBSCRIPTION_DIET_ADD_ACTION);
    }

    public boolean isNotificationSubscriptionDietList(String command) {
        return CommandEnum.COMMAND_NOTIFICATION_SUBSCRIPTION_DIET_LIST.getCommandName().equals(command) || command.equals(NOTIFICATION_SUBSCRIPTION_DIET_LIST_ACTION);
    }

    public boolean isNotificationSubscriptionDietDelete(String command) {
        return CommandEnum.COMMAND_NOTIFICATION_SUBSCRIPTION_DIET_DELETE.getCommandName().equals(command) || command.equals(NOTIFICATION_SUBSCRIPTION_DIET_DELETE_ACTION);
    }

    public boolean isProductButton(String command) {
        return command.equals(PRODUCTS_BUTTON);
    }

    public boolean isProductAddCommand(String command) {
        return CommandEnum.COMMAND_PRODUCTS_ADD.getCommandName().equals(command) || command.equals(PRODUCTS_ADD_ACTION);
    }

    public boolean isProductListCommand(String command) {
        return CommandEnum.COMMAND_PRODUCTS_LIST.getCommandName().equals(command) || command.equals(PRODUCTS_LIST_ACTION);
    }

    public boolean isDietButton(String command) {
        return command.equals(DIET_BUTTON);
    }

    public boolean isDietAddRecordCommand(String command) {
        return CommandEnum.COMMAND_DIET_RECORD_ADD.getCommandName().equals(command) || command.equals(DIET_ADD_ACTION);
    }

    public boolean isDietListRecordCommand(String command) {
        return CommandEnum.COMMAND_DIET_RECORD_LIST.getCommandName().equals(command) || command.equals(DIET_LIST_ACTION);
    }

    public boolean isDietDeleteRecordCommand(String command) {
        return CommandEnum.COMMAND_DIET_RECORD_DELETE.getCommandName().equals(command) || command.equals(DIET_DELETE_ACTION);
    }

    public boolean isDietStatisticDisplayCommand(String command) {
        return CommandEnum.COMMAND_DIET_STATISTIC_DISPLAY.getCommandName().equals(command) || command.equals(Actions.DIET_STATISTIC_DISPLAY_ACTION);
    }

    public boolean isProfileButton(String command) {
        return command.equals(PROFILE_BUTTON);
    }

    public boolean isProfileBiometricDisplayCommand(String command) {
        return CommandEnum.COMMAND_PROFILE_DISPLAY.getCommandName().equals(command) || command.equals(PROFILE_DISPLAY_ACTION);
    }

    public boolean isProfileBiometricUpdateCommand(String command) {
        return CommandEnum.COMMAND_PROFILE_UPDATE.getCommandName().equals(command) || command.equals(PROFILE_UPDATE_ACTION);
    }
}
