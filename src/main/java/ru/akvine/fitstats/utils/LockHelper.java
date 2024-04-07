package ru.akvine.fitstats.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.akvine.commons.cluster.lock.ConcurrentOperationsHelper;

import java.util.concurrent.Callable;

@Component
@RequiredArgsConstructor
public class LockHelper {
    private final ConcurrentOperationsHelper concurrentOperationsHelper;

    public <T> T doWithLock(String lockId, Callable<T> job) {
        T result;

        try {
            result = concurrentOperationsHelper.doOnlineSyncOperation(job, lockId);
        } catch (RuntimeException wrapper) {
            Throwable cause = wrapper.getCause();
            if (cause != null) {
                throw (RuntimeException) cause;
            }

            throw wrapper;
        }

        return result;
    }
}
