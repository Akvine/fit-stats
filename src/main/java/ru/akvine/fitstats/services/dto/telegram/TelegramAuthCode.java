package ru.akvine.fitstats.services.dto.telegram;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.akvine.fitstats.entities.telegram.TelegramAuthCodeEntity;
import ru.akvine.fitstats.services.dto.client.ClientBean;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TelegramAuthCode {
    private Long id;
    private String code;
    private LocalDateTime createdDate;
    private LocalDateTime expiredAt;
    private ClientBean client;

    public TelegramAuthCode(TelegramAuthCodeEntity telegramAuthCodeEntity) {
        this.id = telegramAuthCodeEntity.getId();
        this.code = telegramAuthCodeEntity.getCode();
        this.createdDate = telegramAuthCodeEntity.getCreatedDate();
        this.expiredAt = telegramAuthCodeEntity.getExpiredAt();
        this.client = new ClientBean(telegramAuthCodeEntity.getClient());
    }
}
