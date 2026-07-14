package com.company.backend.shared.application.port.in;

import com.company.backend.shared.domain.ServiceStatus;

public interface GetServiceStatusUseCase {

    ServiceStatus getStatus();
}
