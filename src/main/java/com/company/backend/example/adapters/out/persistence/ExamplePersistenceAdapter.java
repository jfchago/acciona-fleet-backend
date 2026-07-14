package com.company.backend.example.adapters.out.persistence;

import com.company.backend.example.application.port.out.ExamplePersistencePort;
import com.company.backend.example.domain.ExampleRecord;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class ExamplePersistenceAdapter implements ExamplePersistencePort {

    private final ExampleSpringDataRepository repository;

    public ExamplePersistenceAdapter(ExampleSpringDataRepository repository) {
        this.repository = repository;
    }

    @Override
    public ExampleRecord save(ExampleRecord example) {
        return repository.save(ExampleJpaEntity.fromDomain(example)).toDomain();
    }

    @Override
    public Optional<ExampleRecord> findById(UUID id) {
        return repository.findById(id).map(ExampleJpaEntity::toDomain);
    }
}
