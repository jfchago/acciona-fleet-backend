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
    @Column(name="SDN") private String sdn;
    @Column(name="LicencePlate") private String licencePlate;
    @Column(name="StartTerm") private LocalDateTime startTerm;
    @Column(name="StateID") private Integer stateId;
    @Column(name="CancellationDate") private LocalDateTime cancellationDate;
    @Column(name="Term") private BigDecimal term;
    @Column(name="EndTerm") private LocalDateTime endTerm;
    @Column(name="CreditCardLastFour") private String creditCardLastFour;
    @Column(name="Country") private String country;
    @Column(name="PetitionDate") private LocalDateTime petitionDate;
    @Column(name="sysdate") private LocalDateTime sysdate;
    @Column(name="CostCenter") private String costCenter;
    @Column(name="ViaTCard") private String viaTCard;
    @Column(name="ViaTCardRequested") private String viaTCardRequested;
    @Column(name="RegSelecction") private Integer regSelection;
    @Column(name="RegSelecctionUser") private String regSelectionUser;
    Long id(){return id;} String sdn(){return sdn;} String licencePlate(){return licencePlate;} LocalDate startTerm(){return local(startTerm);}
    Integer stateId(){return stateId;} LocalDate cancellationDate(){return local(cancellationDate);} BigDecimal term(){return term;} LocalDate endTerm(){return local(endTerm);}
    String creditCardLastFour(){return creditCardLastFour;} String country(){return country;} String costCenter(){return costCenter;} String viaTCard(){return viaTCard;} String viaTCardRequested(){return viaTCardRequested;} Integer regSelection(){return regSelection;} String regSelectionUser(){return regSelectionUser;} LocalDate updatedAt(){return local(petitionDate);}
    String version(){return sysdate==null?"0":sysdate.toString();}
    private static LocalDate local(LocalDateTime value){return value==null?null:value.toLocalDate();}
}
