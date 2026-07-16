package com.company.backend.carfleetrequests.application.port.out;

import com.company.backend.carfleetrequests.domain.CarFleetRequest;
import java.util.*;

public interface CarFleetRequestWritePort {
    Optional<CarFleetRequest> update(Long id, String expectedVersion, Map<String, Object> changes, Integer state, String actor);
    Optional<CarFleetRequest> duplicate(Long id, String actor);
}
