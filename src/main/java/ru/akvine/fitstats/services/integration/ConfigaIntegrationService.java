package ru.akvine.fitstats.services.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.akvine.fitstats.enums.integration.ConfigaApiMethod;
import ru.akvine.fitstats.services.dto.integration.configa.LoadListProperties;
import ru.akvine.fitstats.services.dto.integration.configa.LoadListPropertiesResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ConfigaIntegrationService {
    @Value("${configa.integration.url}")
    private String url;
    @Value("${configa.integration.app.uuid}")
    private String appUuid;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, String> listProperties() {
        HttpHeaders headers = buildHeaders();
        LoadListProperties loadListProperties = new LoadListProperties().setAppUuid(appUuid);
        HttpEntity<LoadListProperties> httpEntity = new HttpEntity<>(loadListProperties, headers);

        ResponseEntity<LoadListPropertiesResponse> responseEntity = restTemplate.postForEntity(
                url + ConfigaApiMethod.PROPERTIES_LIST.getValue(),
                httpEntity,
                LoadListPropertiesResponse.class
        );
        return buildProperties(Objects.requireNonNull(responseEntity.getBody()));
    }

    private Map<String, String> buildProperties(LoadListPropertiesResponse loadListPropertiesResponse) {
        Map<String, String> properties = new HashMap<>();
        loadListPropertiesResponse.getProperties().forEach(propertyDto -> {
            properties.put(propertyDto.getName(), propertyDto.getName());
        });
        return properties;
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }
}
