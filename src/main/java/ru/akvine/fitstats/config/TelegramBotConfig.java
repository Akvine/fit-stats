package ru.akvine.fitstats.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.meta.generics.TelegramBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.akvine.fitstats.controllers.telegram.bot.DummyTelegramBot;
import ru.akvine.fitstats.controllers.telegram.bot.TelegramLongPoolingBot;
import ru.akvine.fitstats.controllers.telegram.dto.webhook.GetWebhookInfoResponse;
import ru.akvine.fitstats.controllers.telegram.dto.webhook.GetWebhookRequest;
import ru.akvine.fitstats.controllers.telegram.dto.webhook.SetWebhookRequest;
import ru.akvine.fitstats.controllers.telegram.dto.webhook.SetWebhookResponse;
import ru.akvine.fitstats.exceptions.telegram.TelegramConfigurationException;
import ru.akvine.fitstats.services.telegram.MessageHandler;
import ru.akvine.fitstats.services.telegram.MessageProcessor;
import ru.akvine.fitstats.services.telegram.TelegramLongPoolingMessageProcessor;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class TelegramBotConfig {
    private static final String TELEGRAM_API_URL = "https://api.telegram.org/bot";
    private static final String BOT_TYPE_LONGPOOLING = "longpooling";
    private static final String BOT_TYPE_WEBHOOK = "webhook";

    private static final String HTTPS_PROXY_PORT_PROPERTY_NAME = "https.proxyPort";
    private static final String HTTPS_PROXY_HOST_PROPERTY_NAME = "https.proxyHost";

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${telegram.bot.enabled}")
    private boolean isEnabled;
    @Value("${telegram.bot.type}")
    private String botType;
    @Value("${telegram.bot.token}")
    private String botToken;
    @Value("${telegram.bot.username}")
    private String botUsername;
    @Value("${telegram.bot.allowed_updates}")
    private List<String> allowedUpdates;
    @Value("${telegram.bot.webhook-path}")
    private String botWebhookPath;

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
    public TelegramBot telegramBot(MessageHandler messageHandler, DefaultBotOptions defaultBotOptions) throws TelegramApiException {
        if (!isEnabled) {
            return new DummyTelegramBot(defaultBotOptions);
        }

        TelegramBot bot;
        if (BOT_TYPE_LONGPOOLING.equals(botType)) {
            bot = new TelegramLongPoolingBot(defaultBotOptions, botToken, botUsername, messageHandler);
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot((LongPollingBot) bot);
        }  else {
            throw new TelegramConfigurationException("Telegram bot with type = [" + botType + "] is not supported!");
        }
        return bot;
    }

    @Bean
    public MessageProcessor messageProcessor(TelegramBot telegramBot) {
        if (BOT_TYPE_LONGPOOLING.equals(botType)) {
            return new TelegramLongPoolingMessageProcessor((TelegramLongPollingBot) telegramBot);
        } else {
            throw new TelegramConfigurationException("Telegram bot with type = [" + botType + "] is not supported!");
        }
    }

    private void setWebhook(String botToken) {
        GetWebhookInfoResponse getWebhookInfoResponse = sendGetWebhootInfoRequest(botToken);

        if (!getWebhookInfoResponse.isOk() || !botWebhookPath.equals(getWebhookInfoResponse.getResult().getUrl())) {
            SetWebhookResponse setWebhookResponse = sendSetWebhookRequest(botToken, botWebhookPath);
            if (!setWebhookResponse.isOk()) {
                throw new TelegramConfigurationException("Set telegram webhook error");
            }
            getWebhookInfoResponse = sendGetWebhootInfoRequest(botToken);
        }
        if (getWebhookInfoResponse.getResult().isHasCustomeCertificate()) {
            logger.warn("Check ssl certificate");
        }
    }

    private GetWebhookInfoResponse sendGetWebhootInfoRequest(String botToken) {
        GetWebhookRequest request = new GetWebhookRequest(TELEGRAM_API_URL, botToken);
        return restTemplate.getForObject(
                request.getTelegramRequestMethod(),
                GetWebhookInfoResponse.class
        );
    }

    private SetWebhookResponse sendSetWebhookRequest(String botToken, String botPath) {
        SetWebhookRequest request = new SetWebhookRequest(TELEGRAM_API_URL, botToken, botPath);
        request.setAllowedUpdates(allowedUpdates);
        return restTemplate.postForObject(
                request.getTelegramRequestMethod(),
                null,
                SetWebhookResponse.class
        );
    }
}
