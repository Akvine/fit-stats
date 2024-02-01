package ru.akvine.fitstats.services.security;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.akvine.fitstats.entities.security.OtpCounterEntity;
import ru.akvine.fitstats.entities.security.OtpInfo;
import ru.akvine.fitstats.repositories.security.OtpCounterRepository;
import ru.akvine.fitstats.services.notification.NotificationProvider;
import ru.akvine.fitstats.services.notification.dummy.ConstantNotificationService;
import ru.akvine.fitstats.services.properties.PropertyParseService;
import ru.akvine.fitstats.utils.OneTimePasswordGenerator;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpService {
    private final NotificationProvider notificationProvider;
    private final OtpCounterRepository otpCounterRepository;
    private final PropertyParseService propertyParseService;

    private static final String DUMMY_CODE = "1";

    @Value("security.otp.length")
    private String otpLength;
    @Value("security.dummy.notification.provider.enabled")
    private String dummyEnabled;
    @Value("security.otp.max.invalid.attempts")
    private String otpMaxInvalidAttempts;
    @Value("security.otp.lifetime.seconds")
    private String otpLifetimeSeconds;

    public OtpInfo getOneTimePassword(String login) {
        Preconditions.checkNotNull(login, "login is null");

        String value;
        boolean isDummyEnabled = propertyParseService.parseBoolean(dummyEnabled);
        if (isDummyEnabled && notificationProvider instanceof ConstantNotificationService) {
            value = StringUtils.repeat(DUMMY_CODE, propertyParseService.parseInteger(otpLength));
        } else {
            value = OneTimePasswordGenerator.generate(propertyParseService.parseInteger(otpLength));
        }
        int orderNumber = (int) getNextOtpNumber(login);
        logger.info("Otp №{} has been generated for client with email = {}", orderNumber, login);
        return new OtpInfo(
                propertyParseService.parseInteger(otpMaxInvalidAttempts),
                propertyParseService.parseLong(otpLifetimeSeconds),
                orderNumber,
                value);
    }

    @Transactional
    public long getNextOtpNumber(String login) {
        Preconditions.checkNotNull(login, "login is null");

        LocalDateTime now = LocalDateTime.now();
        OtpCounterEntity otpCounter = otpCounterRepository.findByLogin(login);
        if (otpCounter == null) {
            otpCounter = otpCounterRepository.save(
                    new OtpCounterEntity()
                            .setLogin(login)
                    .setLastUpdated(now)
                    .setValue(1)
            );
            return otpCounter.getValue();
        }

        LocalDateTime lastUpdate = otpCounter.getLastUpdated();
        if (isTimeToReset(lastUpdate, now)) {
            otpCounter
                    .setLastUpdated(now)
                    .setValue(1);
            otpCounter = otpCounterRepository.save(otpCounter);
            return otpCounter.getValue();
        }

        long nextValue = otpCounter.getValue() + 1;
        otpCounter
                .setLastUpdated(now)
                .setValue(nextValue);
        otpCounter = otpCounterRepository.save(otpCounter);
        return otpCounter.getValue();
    }

    public static boolean isTimeToReset(LocalDateTime lastUpdate, LocalDateTime now) {
        boolean oneDayPassed = lastUpdate.until(now, ChronoUnit.DAYS) >= 1;
        boolean nextDayHasCome = lastUpdate.getDayOfYear() != now.getDayOfYear();
        return oneDayPassed || nextDayHasCome;
    }
}
