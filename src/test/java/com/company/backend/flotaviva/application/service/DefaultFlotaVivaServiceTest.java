package com.company.backend.flotaviva.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyNoInteractions;

import com.company.backend.flotaviva.application.port.out.FlotaVivaPersistencePort;
import com.company.backend.flotaviva.domain.FlotaVivaRow;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class DefaultFlotaVivaServiceTest {
    private static final Clock CLOCK = Clock.fixed(Instant.parse("2026-07-14T12:00:00Z"), ZoneOffset.UTC);
    private final FlotaVivaPersistencePort persistence = Mockito.mock(FlotaVivaPersistencePort.class);
    private final DefaultFlotaVivaService service = new DefaultFlotaVivaService(persistence, CLOCK);

    @Test
    void get_returnsPageMetadataAndFreshnessForDefaultCountry() {
        var row = row("ES-001");
        when(persistence.findPage(0, 50, "matricula", "ES", "van"))
                .thenReturn(new FlotaVivaPersistencePort.Page(List.of(row), 51));

        var result = service.get(0, 50, "matricula", null, " van ");

        assertThat(result.items()).containsExactly(row);
        assertThat(result.totalElements()).isEqualTo(51);
        assertThat(result.totalPages()).isEqualTo(2);
        assertThat(result.hasNext()).isTrue();
        assertThat(result.freshness().checkedAt()).hasToString("2026-07-14T12:00Z");
    }

    @Test
    void export_csv_containsAllRowsAndEscapesValues() {
        when(persistence.findAll("matricula", "ES", ""))
                .thenReturn(List.of(row("ES,001")));

        var result = service.export("CSV", "matricula", null, null);

        assertThat(result.contentType()).startsWith("text/csv");
        assertThat(new String(result.content(), java.nio.charset.StandardCharsets.UTF_8))
                .contains("Matricula").contains("\"ES,001\"");
    }

    @Test
    void export_rejectsUnsupportedFormatBeforeReadingRows() {
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> service.export("pdf", "matricula", "ES", ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("format must be csv or xlsx");

        verifyNoInteractions(persistence);
    }

    @Test
    void get_rejectsUnsupportedSortBeforeQueryingRows() {
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> service.get(0, 50, "driverName", "ES", ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unsupported sort");

        verifyNoInteractions(persistence);
    }

    @Test
    void get_rejectsOverlongFilterBeforeQueryingRows() {
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> service.get(0, 50, "matricula", "ES", "x".repeat(101)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("filter must not exceed 100 characters");

        verifyNoInteractions(persistence);
    }

    private static FlotaVivaRow row(String matricula) {
        return new FlotaVivaRow(1, null, null, null, null, matricula, null, "Marca", "Modelo", null,
                null, null, null, null, "Activo", null, null, null, null, null, null, null, null, null,
                null, null, null, null, "ES");
    }
}
