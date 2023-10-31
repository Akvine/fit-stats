package ru.akvine.fitstats.controllers.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.fitstats.controllers.rest.converter.ProfileConverter;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.meta.ProfileControllerMeta;
import ru.akvine.fitstats.controllers.rest.validators.ProfileValidator;
import ru.akvine.fitstats.services.ProfileService;
import ru.akvine.fitstats.services.dto.profile.ProfileDownload;

import java.time.LocalDate;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ProfileController implements ProfileControllerMeta {
    private final ProfileConverter profileConverter;
    private final ProfileValidator profileValidator;
    private final ProfileService profileService;

    @Override
    public ResponseEntity downloadRecords(LocalDate startDate,
                                          LocalDate endDate,
                                          String duration,
                                          String converterType) {
        profileValidator.verifyRecordsDownload(startDate, endDate, duration, converterType);
        ProfileDownload profileDownload = profileConverter.convertToProfileDownload(startDate, endDate, duration, converterType);
        byte[] file = profileService.exportRecords(profileDownload);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=file.csv")
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }

    @Override
    public Response uploadRecords() {
        return null;
    }
}