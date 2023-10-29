package ru.akvine.fitstats.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.akvine.fitstats.services.dto.client.ClientBean;

@Service
@Slf4j
@RequiredArgsConstructor
public class PasswordService {
    private final PasswordEncoder passwordEncoder;

    public boolean isValidPassword(ClientBean clientBean, String password) {
        if (StringUtils.isBlank(password)) {
            return false;
        }
        return passwordEncoder.matches(password, clientBean.getPassword());
    }

    @Nullable
    public String encodePassword(String password) {
        if (password == null) {
            return null;
        }
        return passwordEncoder.encode(password);
    }
}
