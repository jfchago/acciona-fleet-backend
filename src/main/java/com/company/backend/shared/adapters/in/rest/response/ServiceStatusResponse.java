package com.company.backend.shared.adapters.in.rest.response;

import com.company.backend.shared.domain.ServiceStatus;

public record ServiceStatusResponse(String status) {

    public static ServiceStatusResponse from(ServiceStatus serviceStatus) {
        return new ServiceStatusResponse(serviceStatus.status());
    }
}
