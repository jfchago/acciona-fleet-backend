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

@Entity
@Table(name="V_CarFleet")
class CarFleetViewEntity {
    @Id private Long id;
    @Column(name="PetitionID") private String petitionId;
    @Column(name="DivisionName") private String divisionName;
    @Column(name="SDN") private String sdn;
    @Column(name="LicencePlate") private String licencePlate;
    @Column(name="StartTerm") private LocalDateTime startTerm;
    @Column(name="StateID") private Integer stateId;
    @Column(name="CancellationDate") private LocalDateTime cancellationDate;
    @Column(name="Term") private BigDecimal term;
    @Column(name="EndTerm") private LocalDateTime endTerm;
    @Column(name="CreditCardLastFour") private String creditCardLastFour;
    @Column(name="Susti") private String substitutionVehicle;
    @Column(name="DriverName") private String driverName;
    @Column(name="DIRECTOR") private String director;
    @Column(name="StateCode") private String stateCode;
    @Column(name="StateDescription") private String stateDescription;
    @Column(name="MonthlyFee") private BigDecimal monthlyFee;
    @Column(name="Contract") private String contract;
    @Column(name="Provider") private String provider;
    @Column(name="VehicleClasification") private String vehicleClassification;
    @Column(name="FuelType") private String fuelType;
    @Column(name="Co2Index") private Integer co2Index;
    @Column(name="EnvironmentalTag") private String environmentalTag;
    @Column(name="Documentation") private Integer documentation;
    @Column(name="PlanMoves") private Integer planMoves;
    @Column(name="RenewableFuel") private Integer renewableFuel;
    @Column(name="Country") private String country;
    @Column(name="PetitionDate") private LocalDateTime petitionDate;
    // The view does not expose sysdate. Native queries populate this read-only
    // field from CarFleet through the explicit version_sysdate alias.
    @Column(name="version_sysdate", insertable=false, updatable=false) private LocalDateTime versionSysdate;
    @Column(name="CostCenter") private String costCenter;
    @Column(name="ViaTCard") private String viaTCard;
    @Column(name="ViaTCardRequested") private String viaTCardRequested;
    @Column(name="RegSelecction") private Integer regSelection;
    @Column(name="RegSelecctionUser") private String regSelectionUser;
    Long id(){return id;} String petitionId(){return petitionId;} String divisionName(){return divisionName;} String sdn(){return sdn;} String licencePlate(){return licencePlate;} LocalDate startTerm(){return local(startTerm);}
    Integer stateId(){return stateId;} LocalDate cancellationDate(){return local(cancellationDate);} BigDecimal term(){return term;} LocalDate endTerm(){return local(endTerm);}
    String creditCardLastFour(){return creditCardLastFour;} String substitutionVehicle(){return substitutionVehicle;} String driverName(){return driverName;} String director(){return director;} String stateCode(){return stateCode;} String stateDescription(){return stateDescription;} BigDecimal monthlyFee(){return monthlyFee;} String contract(){return contract;} String provider(){return provider;} String vehicleClassification(){return vehicleClassification;} String fuelType(){return fuelType;} Integer co2Index(){return co2Index;} String environmentalTag(){return environmentalTag;} Integer documentation(){return documentation;} Integer planMoves(){return planMoves;} Integer renewableFuel(){return renewableFuel;} String country(){return country;} String costCenter(){return costCenter;} String viaTCard(){return viaTCard;} String viaTCardRequested(){return viaTCardRequested;} Integer regSelection(){return regSelection;} String regSelectionUser(){return regSelectionUser;} LocalDate updatedAt(){return local(petitionDate);}
    String version(){return versionSysdate==null?"0":versionSysdate.toString();}
    private static LocalDate local(LocalDateTime value){return value==null?null:value.toLocalDate();}
}
