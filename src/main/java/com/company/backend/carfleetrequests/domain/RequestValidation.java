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
        if (request.contractTermMonths() != null && request.contractTermMonths() <= 0)
            errors.add(new Violation("contractTermMonths", "must be positive"));
        if (request.contractStart() != null && request.contractTermMonths() != null && request.contractTermMonths() > 0) {
            LocalDate expected = request.contractStart().plusMonths(request.contractTermMonths());
            if (request.contractEndDate() != null && !expected.equals(request.contractEndDate()))
                errors.add(new Violation("contractEndDate", "must equal contract start plus contract term"));
        }
        if (request.cardLastFourDigits() != null && !request.cardLastFourDigits().matches("\\d{4}"))
            errors.add(new Violation("cardLastFourDigits", "must contain exactly four digits"));
        return List.copyOf(errors);
    }

    public record Violation(String field, String message) { }
}
