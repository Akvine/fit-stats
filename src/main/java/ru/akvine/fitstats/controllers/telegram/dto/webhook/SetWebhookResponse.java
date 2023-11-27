package ru.akvine.fitstats.controllers.telegram.dto.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SetWebhookResponse {
    private boolean isOk;
}
