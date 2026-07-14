package com.company.backend.shared.adapters.in.rest.controller;

import com.company.backend.shared.application.port.in.GetServiceStatusUseCase;
import com.company.backend.shared.adapters.in.rest.response.ServiceStatusResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/service", produces = MediaType.APPLICATION_JSON_VALUE)
public class ServiceStatusController {

    private final GetServiceStatusUseCase getServiceStatusUseCase;

    public ServiceStatusController(GetServiceStatusUseCase getServiceStatusUseCase) {
        this.getServiceStatusUseCase = getServiceStatusUseCase;
    }

    @GetMapping("/status")
    public ServiceStatusResponse status() {
        return ServiceStatusResponse.from(getServiceStatusUseCase.getStatus());
    }
}
