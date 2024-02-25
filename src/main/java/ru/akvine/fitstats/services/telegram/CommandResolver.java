package ru.akvine.fitstats.services.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.akvine.fitstats.constants.MessageResolverCodes;
import ru.akvine.fitstats.enums.telegram.CommandEnum;
import ru.akvine.fitstats.enums.Language;
import ru.akvine.fitstats.services.MessageResolveService;

@Component
@RequiredArgsConstructor
public class CommandResolver {
    private final MessageResolveService messageResolveService;

    public boolean isStartCommand(String command, Language language) {
        return CommandEnum.COMMAND_START.getCommandName().equals(command) ||
                command.equals(messageResolveService.message(MessageResolverCodes.TELEGRAM_START_CODE, language));
    }

    public boolean isBackCommand(String command, Language language) {
        return CommandEnum.COMMAND_BACK.getCommandName().equals(command) ||
                command.equals(messageResolveService.message(MessageResolverCodes.TELEGRAM_BACK_CODE, language));
    }

    public boolean isHelpCommand(String command, Language language) {
        return CommandEnum.COMMAND_HELP.getCommandName().equals(command) ||
                command.equals(messageResolveService.message(MessageResolverCodes.TELEGRAM_HELP_CODE, language));
    }

    public boolean isNotificationSubscriptionButton(String command, Language language) {
        return CommandEnum.COMMAND_NOTIFICATION_SUBSCRIPTION.getCommandName().equals(command) ||
                command.equals(messageResolveService.message(MessageResolverCodes.TELEGRAM_NOTIFICATION_SUBSCRIPTION_CODE, language));
    }

    public boolean isNotificationSubscriptionDietButton(String command, Language language) {
        return CommandEnum.COMMAND_NOTIFICATION_SUBSCRIPTION_DIET.getCommandName().equals(command) ||
                command.equals(messageResolveService.message(MessageResolverCodes.TELEGRAM_NOTIFICATION_SUBSCRIPTION_DIET_CODE, language));
    }

    public boolean isNotificationSubscriptionDietAdd(String command, Language language) {
        return CommandEnum.COMMAND_NOTIFICATION_SUBSCRIPTION_DIET_ADD.getCommandName().equals(command) ||
                command.equals(messageResolveService.message(MessageResolverCodes.TELEGRAM_NOTIFICATION_SUBSCRIPTION_DIET_ADD_CODE, language));
    }

    public boolean isNotificationSubscriptionDietList(String command, Language language) {
        return CommandEnum.COMMAND_NOTIFICATION_SUBSCRIPTION_DIET_LIST.getCommandName().equals(command) ||
                command.equals(messageResolveService.message(MessageResolverCodes.TELEGRAM_NOTIFICATION_SUBSCRIPTION_DIET_LIST_CODE, language));
    }

    public boolean isNotificationSubscriptionDietDelete(String command, Language language) {
        return CommandEnum.COMMAND_NOTIFICATION_SUBSCRIPTION_DIET_DELETE.getCommandName().equals(command) ||
                command.equals(messageResolveService.message(MessageResolverCodes.TELEGRAM_NOTIFICATION_SUBSCRIPTION_DIET_DELETE_CODE, language));
    }

    public boolean isProductButton(String command, Language language) {
        return CommandEnum.COMMAND_PRODUCTS.getCommandName().equals(command) ||
                command.equals(messageResolveService.message(MessageResolverCodes.TELEGRAM_PRODUCT_CODE, language));

    }

    public boolean isProductAddCommand(String command, Language language) {
        return CommandEnum.COMMAND_PRODUCTS_ADD.getCommandName().equals(command) ||
                command.equals(messageResolveService.message(MessageResolverCodes.TELEGRAM_PRODUCT_ADD_CODE, language));
    }

    public boolean isProductListCommand(String command, Language language) {
        return CommandEnum.COMMAND_PRODUCTS_LIST.getCommandName().equals(command) ||
                command.equals(messageResolveService.message(MessageResolverCodes.TELEGRAM_PRODUCT_LIST_CODE, language));
    }

    public boolean isDietButton(String command, Language language) {
        return CommandEnum.COMMAND_DIET.getCommandName().equals(command) ||
                command.equals(messageResolveService.message(MessageResolverCodes.TELEGRAM_DIET_CODE, language));
    }

    public boolean isDietAddRecordCommand(String command, Language language) {
        return CommandEnum.COMMAND_DIET_RECORD_ADD.getCommandName().equals(command) ||
                command.equals(messageResolveService.message(MessageResolverCodes.TELEGRAM_DIET_ADD_CODE, language));
    }

    public boolean isDietListRecordCommand(String command, Language language) {
        return CommandEnum.COMMAND_DIET_RECORD_LIST.getCommandName().equals(command) ||
                command.equals(messageResolveService.message(MessageResolverCodes.TELEGRAM_DIET_LIST_CODE, language));
    }

    public boolean isDietDeleteRecordCommand(String command, Language language) {
        return CommandEnum.COMMAND_DIET_RECORD_DELETE.getCommandName().equals(command) ||
                command.equals(messageResolveService.message(MessageResolverCodes.TELEGRAM_DIET_DELETE_CODE, language));
    }

    public boolean isDietStatisticDisplayCommand(String command, Language language) {
        return CommandEnum.COMMAND_DIET_STATISTIC_DISPLAY.getCommandName().equals(command) ||
                command.equals(messageResolveService.message(MessageResolverCodes.TELEGRAM_DIET_STATISTIC_DISPLAY_CODE, language));
    }

    public boolean isProfileButton(String command, Language language) {
        return CommandEnum.COMMAND_PROFILE.getCommandName().equals(command) ||
                command.equals(messageResolveService.message(MessageResolverCodes.TELEGRAM_PROFILE_CODE, language));
    }

    public boolean isProfileBiometricDisplayCommand(String command, Language language) {
        return CommandEnum.COMMAND_PROFILE_DISPLAY.getCommandName().equals(command) ||
                command.equals(messageResolveService.message(MessageResolverCodes.TELEGRAM_PROFILE_BIOMETRIC_DISPLAY_CODE, language));
    }

    public boolean isProfileBiometricUpdateCommand(String command, Language language) {
        return CommandEnum.COMMAND_PROFILE_UPDATE.getCommandName().equals(command) ||
                command.equals(messageResolveService.message(MessageResolverCodes.TELEGRAM_PROFILE_UPDATE_CODE, language));
    }

    public boolean isProfileSettingsCommand(String command, Language language) {
        return CommandEnum.COMMAND_PROFILE_SETTINGS.getCommandName().equals(command) ||
                command.equals(messageResolveService.message(MessageResolverCodes.TELEGRAM_PROFILE_SETTINGS_CODE, language));
    }

    public boolean isProfileSettingsUpdateCommand(String command, Language language) {
        return CommandEnum.COMMAND_PROFILE_SETTINGS_UPDATE.getCommandName().equals(command) ||
                command.equals(messageResolveService.message(MessageResolverCodes.TELEGRAM_PROFILE_SETTINGS_UPDATE_CODE, language));
    }

    public boolean isStatisticButton(String command, Language language) {
        return CommandEnum.COMMAND_STATISTIC.getCommandName().equals(command) ||
                command.equals(messageResolveService.message(MessageResolverCodes.TELEGRAM_STATISTIC_CODE, language));
    }

    public boolean isStatisticHistoryCommand(String command, Language language) {
        return CommandEnum.COMMAND_STATISTIC_HISTORY.getCommandName().equals(command) ||
                command.equals(messageResolveService.message(MessageResolverCodes.TELEGRAM_STATISTIC_HISTORY_CODE, language));
    }

    public boolean isStatisticIndicatorsCommand(String command, Language language) {
        return CommandEnum.COMMAND_STATISTIC_INDICATORS.getCommandName().equals(command) ||
                command.equals(messageResolveService.message(MessageResolverCodes.TELEGRAM_STATISTIC_INDICATORS_CODE, language));
    }
}
