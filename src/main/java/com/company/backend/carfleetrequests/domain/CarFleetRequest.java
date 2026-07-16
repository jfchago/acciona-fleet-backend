package com.company.backend.carfleetrequests.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CarFleetRequest(Long id, String sdn, String registration, LocalDate contractStart,
                              Integer state, LocalDate cancellationDate, BigDecimal contractTerm,
                              LocalDate contractEndDate, String cardLastFourDigits, boolean retired,
                              String version, LocalDate updatedAt, String costCenter, String viaTCard,
                              String viaTCardRequested, Integer regSelection, String regSelectionUser) {
    public CarFleetRequest(Long id, String sdn, String registration, LocalDate contractStart, Integer state,
                           LocalDate cancellationDate, BigDecimal contractTerm, LocalDate contractEndDate,
                           String cardLastFourDigits, boolean retired, String version, LocalDate updatedAt) {
        this(id, sdn, registration, contractStart, state, cancellationDate, contractTerm, contractEndDate,
                cardLastFourDigits, retired, version, updatedAt, null, null, null, null, null);
    }
    public static final int CANCELLED_STATE = 14;
    public static final int CLOSED_STATE = 25;
    public static final int ACTIVE_STATE = 11;
    public boolean active() { return !retired; }
    public boolean cardActivationEligible() { return cardLastFourDigits != null && cardLastFourDigits.matches("\\d{4,5}"); }
}
