package ru.akvine.fitstats.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import ru.akvine.commons.cluster.keepalive.KeepAliveService;
import ru.akvine.commons.cluster.lock.ConcurrentOperationsHelper;
import ru.akvine.commons.cluster.lock.DatabaseSLockProvider;
import ru.akvine.commons.cluster.lock.SLockProvider;
import ru.akvine.fitstats.services.properties.PropertyParseService;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class LockConfig {
    private final PropertyParseService propertyParseService;

    @Value("db.lock.check.interval.millis")
    private String dbLockCheckIntervalMillisPropertyName;
    @Value("db.lock.expire.after.created.seconds")
    private String dbLockExpireAfterCreatedSecondsPropertyName;
    @Value("db.lock.max.waiting.threads.per.lock")
    private String dbLockMaxWaitingThreadsPerLockPropertyName;

    @Bean(initMethod = "start", destroyMethod = "destroy")
    public SLockProvider lockProvider(DataSource dataSource, KeepAliveService keepAliveService, PlatformTransactionManager transactionManager) {
        long dbLockCheckIntervalMillis = propertyParseService.parseLong(dbLockCheckIntervalMillisPropertyName);
        long dbLockExpireAfterCreatedSeconds = propertyParseService.parseLong(dbLockExpireAfterCreatedSecondsPropertyName);
        int dbLockMaxWaitingThreadsPerLock = propertyParseService.parseInteger(dbLockMaxWaitingThreadsPerLockPropertyName);
        return new DatabaseSLockProvider(dataSource, transactionManager, keepAliveService, dbLockCheckIntervalMillis, dbLockExpireAfterCreatedSeconds, dbLockMaxWaitingThreadsPerLock);
    }

    @Bean
    public ConcurrentOperationsHelper concurrentOperationsHelper(SLockProvider sLockProvider) {
        return new ConcurrentOperationsHelper(sLockProvider);
    }
}
