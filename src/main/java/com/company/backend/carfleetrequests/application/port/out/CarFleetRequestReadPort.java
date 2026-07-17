package com.company.backend.carfleetrequests.application.port.out;

import com.company.backend.carfleetrequests.domain.*;
import java.util.*;

public interface CarFleetRequestReadPort {
    Page find(RequestVisibility visibility, int page, int size, String sort, String filter);
    Optional<CarFleetRequest> findById(Long id, RequestVisibility visibility);
    boolean existsWithNormalizedSdn(String sdn, Long excludingId);
    record Page(List<CarFleetRequest> items, long totalElements) { }
}
