package com.company.backend.carfleetrequests.domain;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record CarFleetRequest(UUID id, String sdn, String registration, LocalDate contractStart,
                              Integer state, LocalDate cancellationDate, Integer contractTermMonths,
                              LocalDate contractEndDate, String cardLastFourDigits, boolean retired,
                              long version, OffsetDateTime updatedAt) {
    public static final int CANCELLED_STATE = 14;
    public static final int CLOSED_STATE = 25;
    public boolean active() { return !retired; }
    public boolean cardActivationEligible() { return cardLastFourDigits != null && cardLastFourDigits.matches("\\d{4}"); }
}
