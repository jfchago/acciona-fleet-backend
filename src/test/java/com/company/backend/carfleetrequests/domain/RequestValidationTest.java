package com.company.backend.carfleetrequests.domain;

import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class RequestValidationTest {
    @Test
    void state14_withoutCancellationDate_isInvalid() {
        var request = request(14, null, LocalDate.of(2027, 1, 1));

        assertThat(RequestValidation.validate(request))
                .extracting(RequestValidation.Violation::field)
                .contains("cancellationDate");
    }

    @Test
    void validContract_isValidAndCardEligible() {
        var request = request(1, null, LocalDate.of(2026, 1, 31));

        assertThat(RequestValidation.validate(request)).isEmpty();
        assertThat(request.cardActivationEligible()).isTrue();
    }

    private static CarFleetRequest request(int state, LocalDate cancellationDate, LocalDate endDate) {
        return new CarFleetRequest(42L, "SDN-1", "1234ABC", LocalDate.of(2026, 1, 1), state,
                cancellationDate, BigDecimal.ONE, endDate, "1234", false,
                "2026-01-01T00:00", LocalDate.of(2026, 1, 1));
    }
}
