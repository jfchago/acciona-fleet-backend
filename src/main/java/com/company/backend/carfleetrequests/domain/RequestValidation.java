package com.company.backend.carfleetrequests.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class RequestValidation {
    private RequestValidation() { }

    public static List<Violation> validate(CarFleetRequest request) {
        return validate(request, Set.of("sdn", "registration", "contractStart", "state", "cancellationDate", "contractTerm", "cardLastFourDigits"));
    }

    public static List<Violation> validate(CarFleetRequest request, Set<String> changedFields) {
        var errors = new ArrayList<Violation>();
        if (request.sdn() == null || request.sdn().isBlank()) errors.add(new Violation("sdn", "required"));
        if ((changedFields.contains("contractStart") || changedFields.contains("registration")) && request.contractStart() != null && (request.registration() == null || request.registration().isBlank()))
            errors.add(new Violation("registration", "required after contract start"));
        if ((changedFields.contains("state") || changedFields.contains("cancellationDate")) && request.state() != null && (request.state() == CarFleetRequest.CANCELLED_STATE || request.state() == CarFleetRequest.CLOSED_STATE)
                && request.cancellationDate() == null) errors.add(new Violation("cancellationDate", "required for legacy retired state 14 or 25"));
        if ((changedFields.contains("state") || changedFields.contains("cancellationDate")) && request.state() != null && request.state() != CarFleetRequest.CANCELLED_STATE && request.state() != CarFleetRequest.CLOSED_STATE
                && request.cancellationDate() != null) errors.add(new Violation("cancellationDate", "must be empty for an active legacy state"));
        if (changedFields.contains("contractTerm") && request.contractTerm() != null && request.contractTerm().signum() <= 0)
            errors.add(new Violation("contractTerm", "must be positive"));
        if ((changedFields.contains("contractStart") || changedFields.contains("contractTerm")) && request.contractStart() != null && request.contractTerm() == null)
            errors.add(new Violation("contractTerm", "required when contractStart is set"));
        if ((changedFields.contains("contractStart") || changedFields.contains("contractTerm")) && request.contractStart() != null && request.contractTerm() != null && request.contractTerm().signum() > 0) {
            LocalDate expected = request.contractStart().plusMonths(request.contractTerm().longValue()).minusDays(1);
            if (request.contractEndDate() != null && !expected.equals(request.contractEndDate()))
                errors.add(new Violation("contractEndDate", "must equal contract start plus contract term"));
        }
        if (changedFields.contains("cardLastFourDigits") && request.cardLastFourDigits() != null && !request.cardLastFourDigits().matches("\\d{4}"))
            errors.add(new Violation("cardLastFourDigits", "must contain exactly four digits as supported by legacy fixtures"));
        return List.copyOf(errors);
    }

    public record Violation(String field, String message) { }
}
