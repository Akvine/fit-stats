package ru.akvine.fitstats.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import ru.akvine.fitstats.controllers.telegram.bot.DummyTelegramBot;
import ru.akvine.fitstats.controllers.telegram.dto.webhook.GetWebhookInfoResponse;
import ru.akvine.fitstats.controllers.telegram.dto.webhook.GetWebhookRequest;
import ru.akvine.fitstats.controllers.telegram.dto.webhook.SetWebhookRequest;
import ru.akvine.fitstats.controllers.telegram.dto.webhook.SetWebhookResponse;
import ru.akvine.fitstats.exceptions.telegram.TelegramConfigurationException;
import ru.akvine.fitstats.services.properties.PropertyParseService;
import ru.akvine.fitstats.controllers.telegram.bot.TelegramAppWebHookBot;
import ru.akvine.fitstats.services.telegram.handler.MessageHandlerFacade;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class TelegramBotConfig {
    private static final String TELEGRAM_API_URL = "https://api.telegram.org/bot";

    private static final String HTTPS_PROXY_PORT_PROPERTY_NAME = "https.proxyPort";
    private static final String HTTPS_PROXY_HOST_PROPERTY_NAME = "https.proxyHost";

    private final RestTemplate restTemplate = new RestTemplate();
    private final PropertyParseService propertyParseService;

    @Value("telegram.bot.enabled")
    private String enabledPropertyName;
    @Value("telegram.bot.token")
    private String botTokenPropertyName;
    @Value("telegram.bot.username")
    private String botUsernamePropertyName;
    @Value("telegram.bot.allowed_updates")
    private String allowedUpdatesPropertyName;
    @Value("telegram.bot.webhook-path")
    private String botWebhookPathPropertyName;
    @Value("telegram.bot.secret")
    private String botSecretPropertyName;

    private final Environment environment;

    @Bean
    public DefaultBotOptions defaultBotOptions() {
        DefaultBotOptions defaultBotOptions = new DefaultBotOptions();

        if (environment.getProperty(HTTPS_PROXY_PORT_PROPERTY_NAME) == null || environment.getProperty(HTTPS_PROXY_HOST_PROPERTY_NAME) == null) {
            return defaultBotOptions;
        }
        String proxyHost = environment.getProperty(HTTPS_PROXY_HOST_PROPERTY_NAME);
        int proxyPort = Integer.parseInt(environment.getProperty(HTTPS_PROXY_PORT_PROPERTY_NAME));
        DefaultBotOptions.ProxyType proxyType = DefaultBotOptions.ProxyType.HTTP;

        defaultBotOptions.setProxyType(proxyType);
        defaultBotOptions.setProxyHost(proxyHost);
        defaultBotOptions.setProxyPort(proxyPort);
        return defaultBotOptions;
    }

    @Bean
    public TelegramWebhookBot telegramBot(MessageHandlerFacade messageHandlerFacade,
                                          DefaultBotOptions defaultBotOptions) {
        boolean isEnabled = propertyParseService.parseBoolean(enabledPropertyName);
        if (!isEnabled) {
            return new DummyTelegramBot(defaultBotOptions);
        }

        String botToken = propertyParseService.get(botTokenPropertyName);
        String botUsername = propertyParseService.get(botUsernamePropertyName);
        String botPath = propertyParseService.get(botWebhookPathPropertyName);
        String botSecret = propertyParseService.get(botSecretPropertyName);
        TelegramAppWebHookBot bot = new TelegramAppWebHookBot(
                defaultBotOptions,
                botToken,
                messageHandlerFacade);
        bot.setBotUsername(botUsername);
        bot.setBotPath(botPath);
        setWebhook(botToken, botPath, botSecret);
        return bot;
    }

    private void setWebhook(String botToken,
                            String botPath,
                            String botSecret) {
        GetWebhookInfoResponse getWebhookInfoResponse = sendGetWebhookInfoRequest(botToken);
        String botPathWithSecret = StringUtils.endsWith(botPath, "/")
                ? botPath + botSecret
                : botPath + "/" + botSecret;

        if (!getWebhookInfoResponse.isOk() || !botPathWithSecret.equals(getWebhookInfoResponse.getResult().getUrl())) {
            SetWebhookResponse setWebhookResponse = sendSetWebhookRequest(botToken, botPathWithSecret);
            if (!setWebhookResponse.isOk()) {
                throw new TelegramConfigurationException("Set telegram webhook error");
            }
            getWebhookInfoResponse = sendGetWebhookInfoRequest(botToken);
        }
        if (getWebhookInfoResponse.getResult().isHasCustomeCertificate()) {
            logger.warn("Check ssl certificate");
        }
    }

    private GetWebhookInfoResponse sendGetWebhookInfoRequest(String botToken) {
        GetWebhookRequest request = new GetWebhookRequest(TELEGRAM_API_URL, botToken);
        return restTemplate.getForObject(
                request.getTelegramRequestMethod(),
                GetWebhookInfoResponse.class);
    }

    private SetWebhookResponse sendSetWebhookRequest(String botToken, String botPath) {
        SetWebhookRequest request = new SetWebhookRequest(TELEGRAM_API_URL, botToken, botPath);
        List<String> allowedUpdates = propertyParseService.parseToList(allowedUpdatesPropertyName);
        request.setAllowedUpdates(allowedUpdates);

        return restTemplate.postForObject(
                request.getTelegramRequestMethod(),
                null,
                SetWebhookResponse.class);
    }
}
