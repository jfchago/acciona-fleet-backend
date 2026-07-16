package com.company.backend.carfleetrequests.application.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import com.company.backend.carfleetrequests.application.port.out.*;
import com.company.backend.carfleetrequests.domain.*;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.*;

class DefaultCarFleetRequestServiceTest {
    private final CarFleetRequestReadPort reads=mock(CarFleetRequestReadPort.class); private final CarFleetRequestWritePort writes=mock(CarFleetRequestWritePort.class);
    private final CarFleetRequestAuditPort audits=mock(CarFleetRequestAuditPort.class); private final CurrentUserPort users=mock(CurrentUserPort.class); private final CarFleetRequestAuthorizationPort policy=mock(CarFleetRequestAuthorizationPort.class);
    private final UUID id=UUID.randomUUID(); private final CurrentUserPort.User user=new CurrentUserPort.User("alice",Set.of("Access"));
    private final CarFleetRequest current=new CarFleetRequest(id,"SDN-1","1234ABC",LocalDate.of(2026,1,1),1,null,12,LocalDate.of(2027,1,1),"1234",false,4,OffsetDateTime.parse("2026-01-01T00:00:00Z"));
    private DefaultCarFleetRequestService service(){when(users.current()).thenReturn(user);when(policy.allowed(any(),any(),eq(id))).thenReturn(true);when(reads.findById(id,RequestVisibility.ALL)).thenReturn(Optional.of(current));return new DefaultCarFleetRequestService(reads,writes,audits,users,policy);}
    @Test void staleVersion_isRejectedBeforeWrite(){var s=service();assertThatThrownBy(()->s.update(id,3,Map.of("registration","NEW"))).isInstanceOf(CarFleetRequestExceptions.Conflict.class);verifyNoInteractions(writes,audits);}
    @Test void invalidCancellation_isRejectedBeforeWrite(){var s=service();assertThatThrownBy(()->s.update(id,4,Map.of("state",14))).isInstanceOf(CarFleetRequestExceptions.Invalid.class);verifyNoInteractions(writes,audits);}
    @Test void unauthorizedMutation_isRejectedBeforeReadMutation(){when(users.current()).thenReturn(user);when(policy.allowed(any(),eq(CarFleetRequestAuthorizationPort.Action.RETIRE),eq(id))).thenReturn(false);var s=new DefaultCarFleetRequestService(reads,writes,audits,users,policy);assertThatThrownBy(()->s.retire(id,4)).isInstanceOf(CarFleetRequestExceptions.Forbidden.class);verifyNoInteractions(reads,writes,audits);}
    @Test void retire_requiresConditionalWriteAndAuditsActor(){var s=service();var retired=current;when(writes.update(id,4,Map.of("retired",true),true)).thenReturn(Optional.of(retired));s.retire(id,4);verify(writes).update(id,4,Map.of("retired",true),true);verify(audits).append(id,"RETIRE","alice",Map.of("retired",true));}
}
