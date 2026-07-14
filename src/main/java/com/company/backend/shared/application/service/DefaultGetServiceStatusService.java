package com.company.backend.shared.application.service;

import com.company.backend.shared.application.port.in.GetServiceStatusUseCase;
import com.company.backend.shared.domain.ServiceStatus;

public final class DefaultGetServiceStatusService implements GetServiceStatusUseCase {

    @Override
    public ServiceStatus getStatus() {
        return new ServiceStatus("UP");
    }
}
