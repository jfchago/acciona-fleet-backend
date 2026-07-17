package com.company.backend.carfleetrequests.application.port.in;

import com.company.backend.carfleetrequests.domain.*;
import java.util.*;

public interface CarFleetRequestUseCases {
    Page list(RequestVisibility visibility, int page, int size, String sort, String filter);
    CarFleetRequest get(Long id, RequestVisibility visibility);
    UpdateResult update(Long id, String expectedVersion, Map<String, Object> changes);
    CarFleetRequest retire(Long id, String expectedVersion);
    CarFleetRequest reinstate(Long id, String expectedVersion);
    CarFleetRequest duplicate(Long id);
    record UpdateResult(CarFleetRequest request, List<String> warnings) { }
    record Page(List<CarFleetRequest> items, int page, int size, long totalElements) { }
}
