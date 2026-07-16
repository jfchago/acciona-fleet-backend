package com.company.backend.carfleetrequests.adapters.out.persistence;

import com.company.backend.carfleetrequests.application.port.out.*;
import com.company.backend.carfleetrequests.domain.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

interface CarFleetViewRepository extends Repository<CarFleetViewEntity, Long> {
    @Query(value="select v.* from V_CarFleet v join CarFleet c on c.id=v.id where ((v.StateID <> 14 and v.StateID <> 25) or v.StateID is null) and v.Country='ES' and (:filter='' or lower(v.SDN) like lower(concat('%',:filter,'%')) or lower(v.LicencePlate) like lower(concat('%',:filter,'%'))) order by v.PetitionDate, v.id offset :offset rows fetch next :limit rows only", nativeQuery=true)
    List<CarFleetViewEntity> findActive(String filter, int offset, int limit);
    @Query(value="select v.* from V_CarFleet_Bajas v join CarFleet c on c.id=v.id where (:filter='' or lower(v.SDN) like lower(concat('%',:filter,'%')) or lower(v.LicencePlate) like lower(concat('%',:filter,'%'))) order by v.PetitionDate, v.id offset :offset rows fetch next :limit rows only", nativeQuery=true)
    List<CarFleetViewEntity> findAllLegacy(String filter, int offset, int limit);
    @Query(value="select count(*) from V_CarFleet v where ((v.StateID <> 14 and v.StateID <> 25) or v.StateID is null) and v.Country='ES' and (:filter='' or lower(v.SDN) like lower(concat('%',:filter,'%')) or lower(v.LicencePlate) like lower(concat('%',:filter,'%')))", nativeQuery=true)
    long countActive(String filter);
    @Query(value="select count(*) from V_CarFleet_Bajas v where (:filter='' or lower(v.SDN) like lower(concat('%',:filter,'%')) or lower(v.LicencePlate) like lower(concat('%',:filter,'%')))", nativeQuery=true)
    long countAllLegacy(String filter);
    @Query(value="select v.* from V_CarFleet v join CarFleet c on c.id=v.id where v.id=:id and ((v.StateID <> 14 and v.StateID <> 25) or v.StateID is null) and v.Country='ES'", nativeQuery=true)
    Optional<CarFleetViewEntity> findActiveById(Long id);
    @Query(value="select v.* from V_CarFleet_Bajas v join CarFleet c on c.id=v.id where v.id=:id", nativeQuery=true)
    Optional<CarFleetViewEntity> findAllLegacyById(Long id);
    @Query(value="select count(*) from V_CarFleet v where lower(ltrim(rtrim(v.SDN)))=lower(ltrim(rtrim(:sdn))) and v.id<>:id and ((v.StateID <> 14 and v.StateID <> 25) or v.StateID is null) and v.Country='ES'", nativeQuery=true)
    long countDuplicateSdn(String sdn, Long id);
}

