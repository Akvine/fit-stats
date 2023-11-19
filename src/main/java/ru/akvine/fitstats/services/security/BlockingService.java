package ru.akvine.fitstats.services.security;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.entities.security.BlockedCredentialsEntity;
import ru.akvine.fitstats.repositories.security.BlockedCredentialsRepository;

import javax.annotation.concurrent.ThreadSafe;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class BlockingService {
    private final BlockedCredentialsRepository blockedCredentialsRepository;

    private ConcurrentHashMap<String, BlockTime> blockedCache = new ConcurrentHashMap<>();

    @Value("${security.otp.block.time.minutes}")
    private int otpBlockTimeMinutes;

    @Nullable
    public LocalDateTime getUnblockDate(String login) {
        String cacheKey = login;
        BlockTime block = blockedCache.get(cacheKey);
        if (block != null) {
            return block.end;
        }

        BlockedCredentialsEntity blockedCredentialsEntity = blockedCredentialsRepository.findByLogin(login).orElse(null);
        if (blockedCredentialsEntity == null) {
            return null;
        }

        if (blockedCredentialsEntity.getBlockEndDate().isBefore(LocalDateTime.now())) {
            return null;
        }

        BlockTime blockFromDb = new BlockTime(blockedCredentialsEntity);
        synchronized (blockedCache) {
            BlockTime blockFromCache = blockedCache.get(cacheKey);
            if (blockFromCache == null || blockFromCache.end.isBefore(blockFromDb.end)) {
                blockedCache.put(cacheKey, blockFromDb);
                return blockFromDb.end;
            }
            return blockFromCache.end;
        }
    }

    public long setBlock(String login) {
        BlockTime newBlock = new BlockTime(otpBlockTimeMinutes);
        String cacheKey = login;
        blockedCache.put(cacheKey, newBlock);

        BlockedCredentialsEntity newBlockedCredentials = new BlockedCredentialsEntity();
        newBlockedCredentials.setLogin(login);
        newBlockedCredentials.setBlockStartDate(newBlock.start);
        newBlockedCredentials.setBlockEndDate(newBlock.end);

        BlockedCredentialsEntity savedBlockedCredentials = blockedCredentialsRepository.save(newBlockedCredentials);
        return savedBlockedCredentials.getId();
    }

    @Scheduled(fixedDelayString = "${security.blocked.credentials.expired.cache.fixedDelay.milliseconds}")
    public void clearExpiredInCache() {
        boolean[] headerPrinted = new boolean[1];
        blockedCache.entrySet().removeIf(blockEntry -> {
            String login = blockEntry.getKey();
            BlockTime blockInfo = blockEntry.getValue();
            if (LocalDateTime.now().isAfter(blockInfo.end)) {
                if (!headerPrinted[0]) {
                    // TODO : этот блок нужен для того, чтобы в логе написать, что очистка началась
                    headerPrinted[0] = true;
                }
                return true;
            }
            return false;
        });

        if (headerPrinted[0]) {
            // TODO : тоже самое. Здесь должно быть написано в логе, что очистка завершена
        }
    }

    @Scheduled(fixedDelayString = "${security.blocked.credentials.expired.db.fixedDelay.milliseconds}")
    public void clearExpiredInDb() {
        boolean[] headerPrinted = new boolean[1];
        List<BlockedCredentialsEntity> blockedCredentials = blockedCredentialsRepository.findExpired(LocalDateTime.now());
        blockedCredentials.forEach(block -> {
            if (!headerPrinted[0]) {
                // TODO : этот блок нужен для того, чтобы в логе написать, что очистка началась
                headerPrinted[0] = true;
            }
            blockedCredentialsRepository.deleteById(block.getId());
        });

        if (headerPrinted[0]) {
            // TODO : тоже самое. Здесь должно быть написано в логе, что очистка завершена
        }
    }

    @ThreadSafe
    public static class BlockTime {
        final LocalDateTime start;
        final LocalDateTime end;

        public BlockTime(BlockedCredentialsEntity blockedCredentialsEntity) {
            this.start = blockedCredentialsEntity.getBlockStartDate();
            this.end = blockedCredentialsEntity.getBlockEndDate();
        }

        public BlockTime(long howMuchMinutes) {
            this.start = LocalDateTime.now();
            this.end = start.plus(howMuchMinutes, ChronoUnit.MINUTES);
        }
    }
}
