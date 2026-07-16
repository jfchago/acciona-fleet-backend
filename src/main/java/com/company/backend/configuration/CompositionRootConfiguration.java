package com.company.backend.configuration;

import com.company.backend.shared.application.port.in.GetServiceStatusUseCase;
import com.company.backend.shared.application.service.DefaultGetServiceStatusService;
import com.company.backend.example.application.port.out.ExamplePersistencePort;
import com.company.backend.example.application.service.DefaultExampleService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.company.backend.flotaviva.application.service.DefaultFlotaVivaService;
import com.company.backend.flotaviva.application.port.out.FlotaVivaPersistencePort;
import com.company.backend.carfleetrequests.application.port.in.CarFleetRequestUseCases;
import com.company.backend.carfleetrequests.application.port.out.*;
import com.company.backend.carfleetrequests.application.service.DefaultCarFleetRequestService;

@Configuration
public class CompositionRootConfiguration {

    @Bean
    GetServiceStatusUseCase getServiceStatusUseCase() {
        return new DefaultGetServiceStatusService();
    }

    @Bean
    DefaultExampleService exampleService(ExamplePersistencePort examplePersistencePort) {
        return new DefaultExampleService(examplePersistencePort);
    }

    @Bean(name = {"flotaVivaService", "getFlotaVivaUseCase", "exportFlotaVivaUseCase"})
    DefaultFlotaVivaService flotaVivaService(FlotaVivaPersistencePort persistence) {
        return new DefaultFlotaVivaService(persistence);
    }

    @Bean
    CarFleetRequestUseCases carFleetRequestService(CarFleetRequestReadPort reads, CarFleetRequestWritePort writes,
                                                    CarFleetRequestAuditPort audits, CurrentUserPort users,
                                                    CarFleetRequestAuthorizationPort authorization) {
        return new DefaultCarFleetRequestService(reads, writes, audits, users, authorization);
    }
}
