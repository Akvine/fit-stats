package ru.akvine.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.akvine.fitstats.config.SecurityConfig;

@Configuration
@Import({
        SecurityConfig.class
})
public class TestConfiguration {
}
