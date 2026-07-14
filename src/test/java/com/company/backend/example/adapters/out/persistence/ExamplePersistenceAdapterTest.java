package com.company.backend.example.adapters.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.company.backend.example.domain.ExampleRecord;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class ExamplePersistenceAdapterTest {

    @Autowired
    private ExampleSpringDataRepository repository;

    @Test
    void saveAndFindById_roundTripsTheDomainModel() {
        ExamplePersistenceAdapter adapter = new ExamplePersistenceAdapter(repository);
        ExampleRecord input = new ExampleRecord(UUID.randomUUID(), "persisted", OffsetDateTime.parse("2026-01-01T10:00:00Z"));

        ExampleRecord saved = adapter.save(input);
        ExampleRecord found = adapter.findById(input.id()).orElseThrow();

        assertThat(saved).isEqualTo(input);
        assertThat(found).isEqualTo(input);
    }
}
