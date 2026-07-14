package com.company.backend.example.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

/** Neutral technical sample used to demonstrate persistence boundaries. */
public record ExampleRecord(UUID id, String value, OffsetDateTime createdAt) {
}
