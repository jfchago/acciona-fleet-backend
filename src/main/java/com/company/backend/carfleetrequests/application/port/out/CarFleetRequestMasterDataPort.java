package com.company.backend.carfleetrequests.application.port.out;

import com.company.backend.carfleetrequests.domain.State;
import com.company.backend.carfleetrequests.domain.VehicleClassification;
import java.util.List;

public interface CarFleetRequestMasterDataPort {
    List<State> findStates();

    List<VehicleClassification> findVehicleClassificationsForSpain();
}
