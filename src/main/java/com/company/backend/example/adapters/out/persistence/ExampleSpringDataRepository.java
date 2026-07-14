package com.company.backend.example.adapters.out.persistence;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface ExampleSpringDataRepository extends JpaRepository<ExampleJpaEntity, UUID> {
}
