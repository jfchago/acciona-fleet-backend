package com.company.backend.carfleetrequests.application.port.in;

import com.company.backend.carfleetrequests.domain.CarFleetRequest;
import com.company.backend.carfleetrequests.domain.RequestVisibility;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CarFleetRequestUseCases {
    Page list(RequestVisibility visibility, int page, int size, String sort, String filter);
    CarFleetRequest get(UUID id, RequestVisibility visibility);
    CarFleetRequest update(UUID id, long expectedVersion, Map<String, Object> changes);
    CarFleetRequest retire(UUID id, long expectedVersion);
    CarFleetRequest reinstate(UUID id, long expectedVersion);
    record Page(List<CarFleetRequest> items, int page, int size, long totalElements) { }
}
