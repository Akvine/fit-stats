package ru.akvine.fitstats.services;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import ru.akvine.fitstats.enums.Language;

import java.util.Locale;
import java.util.Map;

@RequiredArgsConstructor
public class MessageResolveService {;
    private final MessageSource messageSource;
    private final Map<Language, Locale> locales;


    public String message(String code) {
        return message(code, Language.EN);
    }

    public String message(String code, Language language) {
        return message(code, language, (Object) null);
    }

    public String message(String code, Language language, Object... params) {
        return messageSource.getMessage(code, params, code, locales.get(language));
    }
}
