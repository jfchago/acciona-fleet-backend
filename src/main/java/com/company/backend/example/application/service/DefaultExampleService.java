package com.company.backend.example.application.service;

import com.company.backend.example.application.port.in.CreateExampleUseCase;
import com.company.backend.example.application.port.in.GetExampleUseCase;
import com.company.backend.example.application.port.out.ExamplePersistencePort;
import com.company.backend.example.domain.ExampleRecord;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class DefaultExampleService implements CreateExampleUseCase, GetExampleUseCase {

    private final ExamplePersistencePort persistencePort;
    private final Clock clock;

    public DefaultExampleService(ExamplePersistencePort persistencePort) {
        this(persistencePort, Clock.systemUTC());
    }

    DefaultExampleService(ExamplePersistencePort persistencePort, Clock clock) {
        this.persistencePort = persistencePort;
        this.clock = clock;
    }

    @Override
    public ExampleRecord create(String value) {
        if (value == null || value.isBlank() || value.length() > 255) {
            throw new IllegalArgumentException("value must contain between 1 and 255 characters");
        }
        return persistencePort.save(new ExampleRecord(UUID.randomUUID(), value, OffsetDateTime.now(clock)));
    }

    @Override
    @Transactional(readOnly = true)
    public ExampleRecord get(UUID id) {
        return persistencePort.findById(id).orElseThrow(() -> new ExampleNotFoundException(id));
    }
}
