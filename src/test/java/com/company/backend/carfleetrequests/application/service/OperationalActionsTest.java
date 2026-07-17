package com.company.backend.carfleetrequests.application.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.company.backend.carfleetrequests.application.port.out.*;
import com.company.backend.carfleetrequests.domain.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OperationalActionsTest {
    private final CarFleetRequestReadPort reads = mock(CarFleetRequestReadPort.class);
    private final CarFleetRequestWritePort writes = mock(CarFleetRequestWritePort.class);
    private final CarFleetRequestAuditPort audits = mock(CarFleetRequestAuditPort.class);
    private final CurrentUserPort users = mock(CurrentUserPort.class);
    private final CarFleetRequestAuthorizationPort policy = mock(CarFleetRequestAuthorizationPort.class);
    private final CarFleetRequest request = new CarFleetRequest(42L, "SDN-1", "1234ABC",
            LocalDate.of(2026, 1, 1), 1, null, BigDecimal.ONE, LocalDate.of(2026, 12, 31),
            "1234", false, "v1", LocalDate.of(2026, 1, 1));
    private final CurrentUserPort.User user = new CurrentUserPort.User("alice", Set.of("Access"));

    @Test
    void externalAction_isRejectedBeforeReportingSuccess() {
        var service = service();

        assertThatThrownBy(() -> service.execute(42L, OperationalAction.EMAIL, true))
                .isInstanceOf(CarFleetRequestExceptions.Unavailable.class);

        verifyNoInteractions(writes, audits);
    }

    @Test
    void email_requiresExplicitConfirmation() {
        var service = service();

        assertThatThrownBy(() -> service.execute(42L, OperationalAction.EMAIL, false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("confirmation");

        verifyNoInteractions(writes, audits);
    }

    @Test
    void duplicate_isAuditedWithTheCreatedRequestContext() {
        var copy = new CarFleetRequest(43L, request.sdn(), request.registration(), request.contractStart(),
                request.state(), request.cancellationDate(), request.contractTerm(), request.contractEndDate(),
                request.cardLastFourDigits(), false, "v2", request.updatedAt());
        var service = service();
        when(writes.duplicate(42L, "alice")).thenReturn(Optional.of(copy));

        service.execute(42L, OperationalAction.DUPLICATE, false);

        verify(audits).append(eq(42L), eq("DUPLICATE"), eq("alice"),
                argThat(changes -> changes.get("resultRequestId").equals(43L)));
    }

    private DefaultCarFleetRequestService service() {
        when(users.current()).thenReturn(user);
        when(policy.allowed(any(), any(), eq(42L))).thenReturn(true);
        when(reads.findById(42L, RequestVisibility.ALL)).thenReturn(Optional.of(request));
        return new DefaultCarFleetRequestService(reads, writes, audits, users, policy);
    }
}
