package com.company.backend.flotaviva.adapters.in.rest;

import com.company.backend.flotaviva.application.port.in.ExportFlotaVivaUseCase;
import com.company.backend.flotaviva.application.port.in.GetFlotaVivaUseCase;
import com.company.backend.flotaviva.adapters.in.rest.response.FlotaVivaPageResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;

@RestController
@Validated
@RequestMapping("/api/v1/flota-viva")
public class FlotaVivaController {
    private final GetFlotaVivaUseCase getUseCase;
    private final ExportFlotaVivaUseCase exportUseCase;

    public FlotaVivaController(GetFlotaVivaUseCase getUseCase, ExportFlotaVivaUseCase exportUseCase) {
        this.getUseCase = getUseCase; this.exportUseCase = exportUseCase;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public FlotaVivaPageResponse get(@RequestParam(defaultValue = "0") @Min(0) int page,
                                     @RequestParam(defaultValue = "50") @Min(1) @Max(500) int size,
                                     @RequestParam(defaultValue = "matricula") @Pattern(regexp = "matricula|id|petitionDate|sociedad|marca|modelo") String sort,
                                     @RequestParam(defaultValue = "ES") @Pattern(regexp = "[A-Za-z]{2}") String country,
                                     @RequestParam(defaultValue = "") @Size(max = 100) String filter) {
        var result = getUseCase.get(page, size, sort, country, filter);
        return FlotaVivaPageResponse.from(result);
    }

    @GetMapping(value = "/export")
    public ResponseEntity<byte[]> export(@RequestParam @Pattern(regexp = "(?i)csv|xlsx") String format,
                                         @RequestParam(defaultValue = "matricula") @Pattern(regexp = "matricula|id|petitionDate|sociedad|marca|modelo") String sort,
                                         @RequestParam(defaultValue = "ES") @Pattern(regexp = "[A-Za-z]{2}") String country,
                                         @RequestParam(defaultValue = "") @Size(max = 100) String filter) {
        var result = exportUseCase.export(format, sort, country, filter);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(result.contentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename(result.filename()).build().toString())
                .body(result.content());
    }

}
