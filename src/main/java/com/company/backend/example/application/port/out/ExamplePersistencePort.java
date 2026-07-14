package com.company.backend.example.application.port.out;

import com.company.backend.example.domain.ExampleRecord;
import java.util.Optional;
import java.util.UUID;

public interface ExamplePersistencePort {

    ExampleRecord save(ExampleRecord example);

    Optional<ExampleRecord> findById(UUID id);
}
