package com.company.backend.flotaviva.adapters.out.persistence;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface FlotaVivaSpringDataRepository extends JpaRepository<FlotaVivaJpaEntity, Integer> {
    String FILTER = "(:filter = '' or "
            + "lower(e.divisionFiscalNumber) like lower(concat('%', :filter, '%')) or "
            + "lower(e.sociedad) like lower(concat('%', :filter, '%')) or "
            + "lower(e.nombreSociedad) like lower(concat('%', :filter, '%')) or "
            + "lower(e.divisionGroup) like lower(concat('%', :filter, '%')) or "
            + "lower(e.matricula) like lower(concat('%', :filter, '%')) or "
            + "lower(e.marca) like lower(concat('%', :filter, '%')) or "
            + "lower(e.modelo) like lower(concat('%', :filter, '%')) or "
            + "lower(e.descripcionVehiculo) like lower(concat('%', :filter, '%')) or "
            + "lower(e.motorizacion) like lower(concat('%', :filter, '%')) or "
            + "lower(e.etiqueta) like lower(concat('%', :filter, '%')) or "
            + "lower(str(e.co2)) like lower(concat('%', :filter, '%')) or "
            + "lower(e.proveedor) like lower(concat('%', :filter, '%')) or "
            + "lower(e.clasificacion) like lower(concat('%', :filter, '%')))";

    @Query("select e from FlotaVivaJpaEntity e where e.country = :country and " + FILTER)
    Page<FlotaVivaJpaEntity> findPage(String country, String filter, Pageable pageable);

    @Query("select e from FlotaVivaJpaEntity e where e.country = :country and " + FILTER)
    List<FlotaVivaJpaEntity> findAll(String country, String filter, org.springframework.data.domain.Sort sort);
}
