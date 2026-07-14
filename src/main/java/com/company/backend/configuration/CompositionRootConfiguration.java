package com.company.backend.configuration;

import com.company.backend.shared.application.port.in.GetServiceStatusUseCase;
import com.company.backend.shared.application.service.DefaultGetServiceStatusService;
import com.company.backend.example.application.port.out.ExamplePersistencePort;
import com.company.backend.example.application.service.DefaultExampleService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.company.backend.flotaviva.application.port.in.ExportFlotaVivaUseCase;
import com.company.backend.flotaviva.application.port.in.GetFlotaVivaUseCase;
import com.company.backend.flotaviva.application.service.DefaultFlotaVivaService;
import com.company.backend.flotaviva.application.port.out.FlotaVivaPersistencePort;

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

    @Bean
    DefaultFlotaVivaService flotaVivaService(FlotaVivaPersistencePort persistence) {
        return new DefaultFlotaVivaService(persistence);
    }

    @Bean
    GetFlotaVivaUseCase getFlotaVivaUseCase(DefaultFlotaVivaService service) { return service; }

    @Bean
    ExportFlotaVivaUseCase exportFlotaVivaUseCase(DefaultFlotaVivaService service) { return service; }
}
