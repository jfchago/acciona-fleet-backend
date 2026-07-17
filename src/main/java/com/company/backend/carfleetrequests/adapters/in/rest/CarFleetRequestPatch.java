package com.company.backend.carfleetrequests.adapters.in.rest;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

/** Typed, validated allow-list for partial CarFleetRequest updates. */
public record CarFleetRequestPatch(
        @Size(max = 20) String sdn,
        @Size(max = 10) String registration,
        LocalDate contractStart,
        Integer state,
        LocalDate cancellationDate,
        @DecimalMin(value = "0.0001") @Digits(integer = 14, fraction = 4) BigDecimal contractTerm,
        @Pattern(regexp = "\\d{4}", message = "must contain exactly four digits") String cardLastFourDigits,
        @Pattern(regexp = "[ABab]", message = "must be A or B") String creditCardRequested,
        LocalDate creditCardExpirationDate,
        @Size(max = 50) String codeElement,
        @Size(max = 80) String interiorRegime,
        @Digits(integer = 15, fraction = 4) BigDecimal monthlyFee,
        Integer regSelection,
        @Size(max = 30) String regSelectionUser,
        @Size(max = 20) String costCenter,
        @Size(max = 25) String viaTCard,
        @Pattern(regexp = "[ABab]", message = "must be A or B") String viaTCardRequested,
        @Size(max = 80) String vehicleClassification) {

    Map<String, Object> toChanges() {
        var changes = new LinkedHashMap<String, Object>();
        put(changes, "sdn", sdn); put(changes, "registration", registration); put(changes, "contractStart", contractStart);
        put(changes, "state", state); put(changes, "cancellationDate", cancellationDate); put(changes, "contractTerm", contractTerm);
        put(changes, "cardLastFourDigits", cardLastFourDigits); put(changes, "creditCardRequested", creditCardRequested);
        put(changes, "creditCardExpirationDate", creditCardExpirationDate); put(changes, "codeElement", codeElement);
        put(changes, "interiorRegime", interiorRegime); put(changes, "monthlyFee", monthlyFee); put(changes, "regSelection", regSelection);
        put(changes, "regSelectionUser", regSelectionUser); put(changes, "costCenter", costCenter); put(changes, "viaTCard", viaTCard);
        put(changes, "viaTCardRequested", viaTCardRequested);
        put(changes, "vehicleClassification", vehicleClassification);
        return changes;
    }

    private static void put(Map<String, Object> changes, String key, Object value) {
        if (value != null) changes.put(key, value);
    }
}
