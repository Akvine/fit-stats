package ru.akvine.fitstats.services.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        logger.info("Client with email = {} has been blocked!", login);
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
                    logger.info("Blocking cache cleaning started");
                    headerPrinted[0] = true;
                }
                logger.info("Blocking start = [{}], end = [{}] of client with email = [{}] is expired. Remove it from cache.", blockInfo.start, blockInfo.end, login);
                return true;
            }
            return false;
        });

        if (headerPrinted[0]) {
            logger.info("Blocking cache cleaning ended");
        }
    }

    @Scheduled(fixedDelayString = "${security.blocked.credentials.expired.db.fixedDelay.milliseconds}")
    public void clearExpiredInDb() {
        boolean[] headerPrinted = new boolean[1];
        List<BlockedCredentialsEntity> blockedCredentials = blockedCredentialsRepository.findExpired(LocalDateTime.now());
        blockedCredentials.forEach(block -> {
            if (!headerPrinted[0]) {
                logger.info("BlockedCredentials table cleaning started");
                headerPrinted[0] = true;
            }
            logger.info("Blocking of client with email = {} has expired at = [{}]", block.getLogin(), block.getBlockEndDate());
            blockedCredentialsRepository.deleteById(block.getId());
        });

        if (headerPrinted[0]) {
            logger.info("BlockedCredentials table cleaning ended");
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
