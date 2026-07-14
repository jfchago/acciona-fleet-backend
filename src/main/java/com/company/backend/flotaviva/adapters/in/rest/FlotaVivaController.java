package com.company.backend.flotaviva.adapters.in.rest;

import com.company.backend.flotaviva.application.port.in.ExportFlotaVivaUseCase;
import com.company.backend.flotaviva.application.port.in.GetFlotaVivaUseCase;
import com.company.backend.flotaviva.domain.FlotaVivaRow;
import java.util.List;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/flota-viva")
public class FlotaVivaController {
    private final GetFlotaVivaUseCase getUseCase;
    private final ExportFlotaVivaUseCase exportUseCase;

    public FlotaVivaController(GetFlotaVivaUseCase getUseCase, ExportFlotaVivaUseCase exportUseCase) {
        this.getUseCase = getUseCase; this.exportUseCase = exportUseCase;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResponse get(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "50") int size,
                            @RequestParam(defaultValue = "matricula") String sort, @RequestParam(defaultValue = "ES") String country,
                            @RequestParam(defaultValue = "") String filter) {
        var result = getUseCase.get(page, size, sort, country, filter);
        return new PageResponse(result.items(), result.page(), result.size(), result.totalElements(), result.totalPages(), result.hasNext(), result.freshness());
    }

    @GetMapping(value = "/export")
    public ResponseEntity<byte[]> export(@RequestParam String format, @RequestParam(defaultValue = "matricula") String sort,
                                         @RequestParam(defaultValue = "ES") String country, @RequestParam(defaultValue = "") String filter) {
        var result = exportUseCase.export(format, sort, country, filter);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(result.contentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename(result.filename()).build().toString())
                .body(result.content());
    }

    public record PageResponse(List<FlotaVivaRow> items, int page, int size, long totalElements, int totalPages,
                               boolean hasNext, GetFlotaVivaUseCase.Freshness freshness) { }
}
