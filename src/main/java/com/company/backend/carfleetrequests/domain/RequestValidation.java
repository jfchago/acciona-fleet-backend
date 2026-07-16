package com.company.backend.carfleetrequests.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class RequestValidation {
    private RequestValidation() { }

    public static List<Violation> validate(CarFleetRequest request) {
        var errors = new ArrayList<Violation>();
        if (request.sdn() == null || request.sdn().isBlank()) errors.add(new Violation("sdn", "required"));
        if (request.contractStart() != null && (request.registration() == null || request.registration().isBlank()))
            errors.add(new Violation("registration", "required after contract start"));
        if ((request.state() != null && (request.state() == CarFleetRequest.CANCELLED_STATE || request.state() == CarFleetRequest.CLOSED_STATE))
                && request.cancellationDate() == null) errors.add(new Violation("cancellationDate", "required for state 14 or 25"));
        if (request.contractTerm() != null && request.contractTerm().signum() <= 0)
            errors.add(new Violation("contractTerm", "must be positive"));
        if (request.contractStart() != null && request.contractTerm() == null)
            errors.add(new Violation("contractTerm", "required when contractStart is set"));
        if (request.contractStart() != null && request.contractTerm() != null && request.contractTerm().signum() > 0) {
            LocalDate expected = request.contractStart().plusMonths(request.contractTerm().longValue()).minusDays(1);
            if (request.contractEndDate() != null && !expected.equals(request.contractEndDate()))
                errors.add(new Violation("contractEndDate", "must equal contract start plus contract term"));
        }
        if (request.cardLastFourDigits() != null && !request.cardLastFourDigits().matches("\\d{4,5}"))
            errors.add(new Violation("cardLastFourDigits", "must contain four or five digits"));
        return List.copyOf(errors);
    }

    public record Violation(String field, String message) { }
}
