package ru.akvine.api.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import ru.akvine.fitstats.config.SecurityConfig;

@Configuration
@Import({
        SecurityConfig.class
})
@ComponentScan({
        "ru.akvine.fitstats"
})
public class TestConfiguration extends WebMvcConfigurationSupport {
}
