package ru.akvine.fitstats.api.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.akvine.fitstats.config.SecurityConfig;

@Configuration
@Import({
        SecurityConfig.class
})
@ComponentScan({
        "ru.akvine.fitstats"
})
public class TestConfiguration {
}
