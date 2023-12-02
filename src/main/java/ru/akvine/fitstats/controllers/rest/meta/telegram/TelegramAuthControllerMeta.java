package ru.akvine.fitstats.controllers.rest.meta.telegram;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;

@RequestMapping(value = "/telegram/auth")
public interface TelegramAuthControllerMeta {
    @PostMapping(value = "/code/generate")
    Response generate();

    @PostMapping(value = "/code/get")
    Response get();
}
