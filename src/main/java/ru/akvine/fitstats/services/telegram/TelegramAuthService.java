package ru.akvine.fitstats.services.telegram;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.akvine.fitstats.context.TelegramAuthContext;
import ru.akvine.fitstats.entities.ClientEntity;
import ru.akvine.fitstats.entities.telegram.TelegramAuthCodeEntity;
import ru.akvine.fitstats.exceptions.telegram.TelegramAuthCodeNotFoundException;
import ru.akvine.fitstats.repositories.telegram.TelegramAuthRepository;
import ru.akvine.fitstats.services.client.ClientService;
import ru.akvine.fitstats.services.dto.client.ClientBean;
import ru.akvine.fitstats.services.dto.telegram.TelegramAuthCode;
import ru.akvine.fitstats.services.dto.telegram.TelegramSubscriptionBean;
import ru.akvine.fitstats.services.properties.PropertyParseService;
import ru.akvine.fitstats.utils.RandomCodeGenerator;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramAuthService {
    private final ClientService clientService;
    private final TelegramAuthRepository telegramAuthCodeRepository;
    private final TelegramSubscriptionService telegramSubscriptionService;
    private final PropertyParseService propertyParseService;

    private static final Long TELEGRAM_SUBSCRIPTION_TYPE = 1L;

    @Value("telegram.bot.authcode.length")
    private String authCodeLength;
    @Value("telegram.bot.authcode.lifetime.seconds")
    private String authCodeLifetimeSeconds;

    @Transactional
    public TelegramAuthCode generate(String clientUuid) {
        Preconditions.checkNotNull(clientUuid, "clientUuid is null");
        ClientEntity client = clientService.verifyExistsByUuidAndGet(clientUuid);

        telegramAuthCodeRepository.deleteAllByClientId(client.getId());
        String code = RandomCodeGenerator.generateNewRandomCode(propertyParseService.parseInteger(authCodeLength));
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiredAt = now.plusSeconds(propertyParseService.parseInteger(authCodeLifetimeSeconds));

        TelegramAuthCodeEntity telegramAuthCodeEntity = new TelegramAuthCodeEntity()
                .setCode(code)
                .setClient(client)
                .setCreatedDate(now)
                .setExpiredAt(expiredAt);

        return new TelegramAuthCode(telegramAuthCodeRepository.save(telegramAuthCodeEntity));
    }

    public TelegramAuthCode getByClientUuid(String clientUuid) {
        Preconditions.checkNotNull(clientUuid, "clientUuid is null");

        TelegramAuthCodeEntity telegramAuthCodeEntity = telegramAuthCodeRepository.findByClientUuid(clientUuid)
                .orElseThrow(() -> new TelegramAuthCodeNotFoundException("Expired telegram auth code for clientUuid = [" + clientUuid + "]"));

        if (telegramAuthCodeEntity.isExpired()) {
            throw new TelegramAuthCodeNotFoundException("Expired telegram auth code for clientUuid = [" + clientUuid + "]");
        }
        return new TelegramAuthCode(telegramAuthCodeEntity);
    }

    public void deleteById(Long authCodeId) {
        Preconditions.checkNotNull(authCodeId, "authCodeId is null");
        telegramAuthCodeRepository.deleteById(authCodeId);
    }

    public TelegramAuthCode getByAuthCode(String authCode) throws TelegramAuthCodeNotFoundException {
        Preconditions.checkNotNull(authCode, "authCode is null");

        return new TelegramAuthCode(
                telegramAuthCodeRepository
                        .findByAuthCode(authCode)
                        .orElseThrow(() -> new TelegramAuthCodeNotFoundException("Telegram auth code not found!"))
        );
    }

    public void authenticateTelegramClient(String text, Long telegramId, String chatId) {
        TelegramAuthCode authCode = getByAuthCode(text);
        telegramSubscriptionService.save(telegramId, authCode, chatId, TELEGRAM_SUBSCRIPTION_TYPE);
        deleteById(authCode.getId());
    }

    public ClientBean getAuthenticateUser(Update update) {
        Long telegramId = update.getMessage().getFrom().getId();
        TelegramSubscriptionBean telegramSubscriptionBean = telegramSubscriptionService.findByTelegramId(telegramId);
        authenticate(telegramSubscriptionBean.getClient().getEmail());
        return telegramSubscriptionBean.getClient();
    }

    private void authenticate(String email) {
        TelegramAuthContext.set(email);
    }
}
