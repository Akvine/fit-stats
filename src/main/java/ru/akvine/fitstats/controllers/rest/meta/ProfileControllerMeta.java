package ru.akvine.fitstats.controllers.rest.meta;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;

import java.time.LocalDate;

@RequestMapping(value = "/profile")
public interface ProfileControllerMeta {
    @PostMapping(value = "/records/export")
    ResponseEntity downloadRecords(@RequestParam(value = "startDate", required = false) LocalDate startDate,
                                   @RequestParam(value = "endDate", required = false) LocalDate endDate,
                                   @RequestParam(value = "duration", required = false) String duration,
                                   @RequestParam(value = "converterType", required = false) String converterType);

    @PostMapping(value = "/records/import")
    Response uploadRecords();
}
