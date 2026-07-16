package com.company.backend.carfleetrequests.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import org.mockito.ArgumentCaptor;
import com.company.backend.carfleetrequests.application.port.out.*;
import com.company.backend.carfleetrequests.domain.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DefaultCarFleetRequestServiceTest {
    private final CarFleetRequestReadPort reads = mock(CarFleetRequestReadPort.class);
    private final CarFleetRequestWritePort writes = mock(CarFleetRequestWritePort.class);
    private final CarFleetRequestAuditPort audits = mock(CarFleetRequestAuditPort.class);
    private final CurrentUserPort users = mock(CurrentUserPort.class);
    private final CarFleetRequestAuthorizationPort policy = mock(CarFleetRequestAuthorizationPort.class);
    private final Long id = 42L;
    private final CurrentUserPort.User user = new CurrentUserPort.User("alice", Set.of("Access"));
    private final CarFleetRequest current = new CarFleetRequest(id, "SDN-1", "1234ABC",
            LocalDate.of(2026, 1, 1), 1, null, BigDecimal.ONE, LocalDate.of(2026, 12, 31),
            "1234", false, "2026-01-01T00:00", LocalDate.of(2026, 1, 1));

    @Test
    void staleVersion_isRejectedBeforeWrite() {
        var service = service();

        assertThatThrownBy(() -> service.update(id, "2025-01-01T00:00", Map.of("registration", "NEW")))
                .isInstanceOf(CarFleetRequestExceptions.Conflict.class);

        verifyNoInteractions(writes, audits);
    }

    @Test
    void invalidCancellation_isRejectedBeforeWrite() {
        var service = service();

        assertThatThrownBy(() -> service.update(id, current.version(), Map.of("state", 14)))
                .isInstanceOf(CarFleetRequestExceptions.Invalid.class);

        verifyNoInteractions(writes, audits);
    }

    @Test
    void unauthorizedMutation_isRejectedBeforeReadMutation() {
        when(users.current()).thenReturn(user);
        when(policy.allowed(any(), eq(CarFleetRequestAuthorizationPort.Action.RETIRE), eq(id))).thenReturn(false);
        var service = new DefaultCarFleetRequestService(reads, writes, audits, users, policy);

        assertThatThrownBy(() -> service.retire(id, current.version()))
                .isInstanceOf(CarFleetRequestExceptions.Forbidden.class);

        verifyNoInteractions(reads, writes, audits);
    }

    @Test
    void retire_updatesLegacyCancellationStateAndAuditsActor() {
        var service = service();
        var retired = new CarFleetRequest(id, current.sdn(), current.registration(), current.contractStart(), 14,
                LocalDate.of(2026, 1, 2), current.contractTerm(), current.contractEndDate(), current.cardLastFourDigits(),
                true, "2026-01-02T00:00", LocalDate.of(2026, 1, 2));
        when(writes.update(eq(id), eq(current.version()), anyMap(), eq(14), eq("alice"))).thenReturn(Optional.of(retired));

        service.retire(id, current.version());

        var changes = ArgumentCaptor.forClass(Map.class);
        verify(writes).update(eq(id), eq(current.version()), changes.capture(), eq(14), eq("alice"));
        assertThat(changes.getValue()).containsKeys("state", "cancellationDate").containsEntry("state", 14);
        verify(audits).append(eq(id), eq("RETIRE"), eq("alice"), eq(changes.getValue()));
    }

    private DefaultCarFleetRequestService service() {
        when(users.current()).thenReturn(user);
        when(policy.allowed(any(), any(), eq(id))).thenReturn(true);
        when(reads.findById(id, RequestVisibility.ALL)).thenReturn(Optional.of(current));
        return new DefaultCarFleetRequestService(reads, writes, audits, users, policy);
    }
}
