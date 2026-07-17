package com.company.backend.flotaviva.application.port.in;

import com.company.backend.flotaviva.domain.FlotaVivaRow;
import java.time.OffsetDateTime;
import java.util.List;

public interface GetFlotaVivaUseCase {
    Result get(int page, int size, String sort, String country, String filter);

    record Result(List<FlotaVivaRow> items, int page, int size, long totalElements,
                  int totalPages, boolean hasNext, Freshness freshness) { }

    record Freshness(OffsetDateTime checkedAt, String status) { }
}
