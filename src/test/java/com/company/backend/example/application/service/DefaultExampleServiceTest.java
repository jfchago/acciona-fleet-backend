package com.company.backend.example.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.company.backend.example.application.port.out.ExamplePersistencePort;
import com.company.backend.example.domain.ExampleRecord;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class DefaultExampleServiceTest {

    private static final Clock FIXED_CLOCK = Clock.fixed(Instant.parse("2026-01-01T10:00:00Z"), ZoneOffset.UTC);

    @Test
    void create_persistsNeutralExampleWithGeneratedIdAndTimestamp() {
        ExamplePersistencePort port = org.mockito.Mockito.mock(ExamplePersistencePort.class);
        when(port.save(org.mockito.ArgumentMatchers.any())).thenAnswer(invocation -> invocation.getArgument(0));
        DefaultExampleService service = new DefaultExampleService(port, FIXED_CLOCK);

        ExampleRecord result = service.create("sample value");

        assertThat(result.id()).isNotNull();
        assertThat(result.value()).isEqualTo("sample value");
        assertThat(result.createdAt()).hasToString("2026-01-01T10:00Z");
        verify(port).save(org.mockito.ArgumentMatchers.any(ExampleRecord.class));
    }

    @Test
    void get_missingExample_throwsNotFound() {
        ExamplePersistencePort port = org.mockito.Mockito.mock(ExamplePersistencePort.class);
        UUID id = UUID.randomUUID();
        when(port.findById(id)).thenReturn(Optional.empty());
        DefaultExampleService service = new DefaultExampleService(port, FIXED_CLOCK);

        assertThatThrownBy(() -> service.get(id)).isInstanceOf(ExampleNotFoundException.class);
    }
}
