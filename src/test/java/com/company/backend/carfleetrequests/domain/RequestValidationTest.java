package com.company.backend.carfleetrequests.domain;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.*;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class RequestValidationTest {
    @Test void state14_withoutCancellationDate_isInvalid() {
        var r=new CarFleetRequest(UUID.randomUUID(),"SDN-1","1234ABC",LocalDate.of(2026,1,1),14,null,12,LocalDate.of(2027,1,1),"1234",false,0,OffsetDateTime.parse("2026-01-01T00:00:00Z"));
        assertThat(RequestValidation.validate(r)).extracting(RequestValidation.Violation::field).contains("cancellationDate");
    }
    @Test void validContract_isValidAndCardEligible() {
        var r=new CarFleetRequest(UUID.randomUUID(),"SDN-1","1234ABC",LocalDate.of(2026,1,1),1,null,12,LocalDate.of(2027,1,1),"1234",false,0,OffsetDateTime.parse("2026-01-01T00:00:00Z"));
        assertThat(RequestValidation.validate(r)).isEmpty(); assertThat(r.cardActivationEligible()).isTrue();
    }
}
