package com.company.backend.carfleetrequests.adapters.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.Column;
import java.lang.reflect.Field;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.Query;

class CarFleetViewRepositoryContractTest {
    @Test
    void viewMapping_doesNotRequireVersionColumnFromView() throws NoSuchFieldException {
        Field version = CarFleetViewEntity.class.getDeclaredField("versionSysdate");

        assertThat(version.getAnnotation(Column.class).name()).isEqualTo("version_sysdate");
        assertThat(Arrays.stream(CarFleetViewEntity.class.getDeclaredFields())
                .map(field -> field.getAnnotation(Column.class))
                .filter(annotation -> annotation != null)
                .map(Column::name))
                .doesNotContain("sysdate");
    }

    @Test
    void activeQuery_keepsLegacyVisibilityAndLoadsVersionFromTable() throws NoSuchMethodException {
        String query = CarFleetViewRepository.class.getDeclaredMethod("findActive", String.class, int.class, int.class)
                .getAnnotation(Query.class).value();

        assertThat(query).contains("from V_CarFleet v")
                .contains("join CarFleet c on c.id=v.id")
                .contains("c.sysdate as version_sysdate")
                .contains("((v.StateID <> 14 and v.StateID <> 25) or v.StateID is null)")
                .contains("v.Country='ES'");
    }

    @Test
    void allQueries_useOnlyLegacyRetiredView() throws NoSuchMethodException {
        String listQuery = CarFleetViewRepository.class.getDeclaredMethod("findAllLegacy", String.class, int.class, int.class)
                .getAnnotation(Query.class).value();
        String countQuery = CarFleetViewRepository.class.getDeclaredMethod("countAllLegacy", String.class)
                .getAnnotation(Query.class).value();
        String byIdQuery = CarFleetViewRepository.class.getDeclaredMethod("findAllLegacyById", Long.class)
                .getAnnotation(Query.class).value();

        assertThat(listQuery).contains("from V_CarFleet_Bajas b").doesNotContain("V_CarFleet v").contains("c.sysdate as version_sysdate");
        assertThat(countQuery).contains("from V_CarFleet_Bajas b").doesNotContain("V_CarFleet v");
        assertThat(byIdQuery).contains("from V_CarFleet_Bajas b").doesNotContain("V_CarFleet v").contains("c.sysdate as version_sysdate");
    }
}
