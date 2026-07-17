package com.company.backend.flotaviva.adapters.in.rest.response;

import com.company.backend.flotaviva.application.port.in.GetFlotaVivaUseCase;
import java.time.OffsetDateTime;
import java.util.List;

public record FlotaVivaPageResponse(List<FlotaVivaRowResponse> items, int page, int size,
                                    long totalElements, int totalPages, boolean hasNext,
                                    FreshnessResponse freshness) {

    public static FlotaVivaPageResponse from(GetFlotaVivaUseCase.Result result) {
        return new FlotaVivaPageResponse(result.items().stream().map(FlotaVivaRowResponse::from).toList(),
                result.page(), result.size(), result.totalElements(), result.totalPages(), result.hasNext(),
                FreshnessResponse.from(result.freshness()));
    }

    public record FreshnessResponse(OffsetDateTime checkedAt, String status) {
        private static FreshnessResponse from(GetFlotaVivaUseCase.Freshness freshness) {
            return new FreshnessResponse(freshness.checkedAt(), freshness.status());
        }
    }
}
