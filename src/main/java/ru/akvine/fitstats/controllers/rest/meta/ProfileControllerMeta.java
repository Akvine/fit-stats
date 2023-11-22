package ru.akvine.fitstats.controllers.rest.meta;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.akvine.fitstats.controllers.rest.dto.common.Response;
import ru.akvine.fitstats.controllers.rest.dto.profile.UpdateBiometricRequest;

import javax.validation.Valid;
import java.time.LocalDate;

@RequestMapping(value = "/profile")
public interface ProfileControllerMeta {
    @PostMapping(value = "/records/export")
    ResponseEntity exportRecords(@RequestParam(value = "startDate", required = false) LocalDate startDate,
                                 @RequestParam(value = "endDate", required = false) LocalDate endDate,
                                 @RequestParam(value = "duration", required = false) String duration,
                                 @RequestParam(value = "filename", required = false) String filename,
                                 @RequestParam(value = "converterType", required = false) String converterType);

    @PostMapping(value = "/records/import")
    Response importRecords(@RequestParam(value = "converterType", defaultValue = "CSV") String converterType,
                           @RequestParam(value = "file") MultipartFile file);

    @PostMapping(value = "/biometric/update")
    Response updateBiometric(@Valid @RequestBody UpdateBiometricRequest request);

    @GetMapping(value = "/biometric/display")
    Response displayBiometric();
}
