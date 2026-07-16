package com.company.backend.carfleetrequests.application.port.out;
import com.company.backend.carfleetrequests.domain.CarFleetRequest;
import java.util.*;
public interface CarFleetRequestWritePort {
    Optional<CarFleetRequest> update(UUID id, long expectedVersion, Map<String,Object> changes, boolean retired);
}
