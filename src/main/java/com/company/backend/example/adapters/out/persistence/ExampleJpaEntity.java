package com.company.backend.example.adapters.out.persistence;

import com.company.backend.example.domain.ExampleRecord;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "examples")
class ExampleJpaEntity {

    @Id
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;
    @Column(name = "example_value", nullable = false, length = 255)
    private String value;
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected ExampleJpaEntity() {
    }

    private ExampleJpaEntity(UUID id, String value, OffsetDateTime createdAt) {
        this.id = id;
        this.value = value;
        this.createdAt = createdAt;
    }

    static ExampleJpaEntity fromDomain(ExampleRecord example) {
        return new ExampleJpaEntity(example.id(), example.value(), example.createdAt());
    }

    ExampleRecord toDomain() {
        return new ExampleRecord(id, value, createdAt);
    }
}
