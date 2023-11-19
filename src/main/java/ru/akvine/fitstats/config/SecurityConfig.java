package ru.akvine.fitstats.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.akvine.fitstats.config.security.RestAuthenticationEntryPoint;
import ru.akvine.fitstats.config.security.RestSuccessLogoutHandler;
import ru.akvine.fitstats.services.notification.NotificationProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${spring.session.cookie-name}")
    private String sessionCookieName;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().disable()
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers("/clients/register").permitAll()
                        .antMatchers("/clients/login").permitAll()
                        .antMatchers("/clients/logout").permitAll()
                        .antMatchers("/registration/**").permitAll()
                        .antMatchers("/auth/**").permitAll()
                        .antMatchers("/actuator/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint())
                .and()
                .logout()
                .deleteCookies(sessionCookieName)
                .logoutSuccessHandler(restSuccessLogoutHandler())
                .invalidateHttpSession(true);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestSuccessLogoutHandler restSuccessLogoutHandler() {
        return new RestSuccessLogoutHandler();
    }

    @Bean
    public RestAuthenticationEntryPoint restAuthenticationEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }
}
