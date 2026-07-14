package com.company.backend.flotaviva.adapters.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FlotaVivaSpringDataRepositoryTest {
    @Autowired private EntityManager entityManager;
    @Autowired private FlotaVivaSpringDataRepository repository;

    @BeforeEach
    void insertFixture() {
        entityManager.createNativeQuery("""
                insert into dbo.v_flota_viva (id, "Country", "Matricula", "Marca", "Modelo", "Sociedad", "DivisionGroup", "CO2")
                values (1, 'ES', 'TEST-001', 'VOLVO', 'XC90', 'ACCIONA', 'FLEET', 30),
                       (2, 'ES', 'TEST-002', 'RENAULT', 'ARKANA', 'ACCIONA', 'FLEET', 107),
                       (3, 'PT', 'TEST-003', 'VOLVO', 'XC60', 'ACCIONA', 'FLEET', 20)
                """).executeUpdate();
        entityManager.clear();
    }

    @Test
    void findPage_filtersByCountryAndLegacyTextAndAddsStableIdOrder() {
        var result = repository.findPage("ES", "volvo", PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).extracting(FlotaVivaJpaEntity::toDomain)
                .extracting("matricula").containsExactly("TEST-001");
    }
}
