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
import jakarta.persistence.Transient;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(name = "CarFleet")
class CarFleetEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="SDN", length=20) private String sdn;
    @Column(name="LicencePlate", length=10) private String licencePlate;
    @Column(name="StartTerm") private LocalDateTime startTerm;
    @Column(name="StateID") private Integer stateId;
    @Column(name="CancellationDate") private LocalDateTime cancellationDate;
    @Column(name="Term", precision=18, scale=4) private BigDecimal term;
    @Column(name="EndTerm") private LocalDateTime endTerm;
    @Column(name="CreditCardLastFour", length=5) private String creditCardLastFour;
    @Column(name="CreditCardnumber", length=20) private String creditCardNumber;
    @Column(name="CreditCardRequested", length=1) private String creditCardRequested;
    @Column(name="CreditCardExpirationDate") private LocalDateTime creditCardExpirationDate;
    @Column(name="CodeElement", length=50) private String codeElement;
    // InteriorRegime belongs to the legacy Contract table/view, not to dbo.CarFleet.
    // Keep it transient so Hibernate never includes it in CarFleet INSERT/UPDATE statements.
    @Transient private String interiorRegime;
    @Column(name="MonthlyFee", precision=19, scale=4) private BigDecimal monthlyFee;
    @Column(name="CostCenter", length=20) private String costCenter;
    @Column(name="ViaTCard", length=25) private String viaTCard;
    @Column(name="ViaTCardRequested", length=1) private String viaTCardRequested;
    @Column(name="RegSelecction") private Integer regSelection;
    @Column(name="RegSelecctionUser", length=30) private String regSelectionUser;
    @Column(name="PlanMoves") private Integer planMoves;
    @Column(name="RenewableFuel") private Integer renewableFuel;
    @Column(name="VehicleClasification", length=80) private String vehicleClassification;
    @Column(name="sysuser", length=50) private String sysuser;
    @Column(name="PetitionDate") private LocalDateTime petitionDate;
    @Column(name="sysdate") private LocalDateTime sysdate;
    Long id(){return id;} String sdn(){return sdn;} String licencePlate(){return licencePlate;} Integer stateId(){return stateId;} LocalDateTime petitionDate(){return petitionDate;}
    String creditCardNumber(){return creditCardNumber;} String creditCardLastFour(){return creditCardLastFour;} LocalDate startTerm(){return date(startTerm);} BigDecimal term(){return term;}
    String codeElement(){return codeElement;} String interiorRegime(){return interiorRegime;} BigDecimal monthlyFee(){return monthlyFee;} String costCenter(){return costCenter;} String viaTCard(){return viaTCard;} String viaTCardRequested(){return viaTCardRequested;} Integer regSelection(){return regSelection;} String regSelectionUser(){return regSelectionUser;} Integer planMoves(){return planMoves;} Integer renewableFuel(){return renewableFuel;}
    void setSdn(String x){sdn=x;} void setLicencePlate(String x){licencePlate=x;} void setStartTerm(LocalDate x){startTerm=x==null?null:x.atStartOfDay();}
    void setStateId(Integer x){stateId=x;} void setCancellationDate(LocalDate x){cancellationDate=x==null?null:x.atStartOfDay();}
    void setVehicleClassification(String x){vehicleClassification=x;}
    void setTerm(BigDecimal x){term=x;} void setEndTerm(LocalDate x){endTerm=x==null?null:x.atStartOfDay();}
    void setCreditCardLastFour(String x){creditCardLastFour=x;} void setCodeElement(String x){codeElement=x;} void setInteriorRegime(String x){interiorRegime=x;} void setCreditCardRequested(String x){creditCardRequested=x;} void setCreditCardExpirationDate(LocalDate x){creditCardExpirationDate=x==null?null:x.atStartOfDay();}
    void setMonthlyFee(BigDecimal x){monthlyFee=x;} void setCostCenter(String x){costCenter=x;} void setViaTCard(String x){viaTCard=x;} void setViaTCardRequested(String x){viaTCardRequested=x;} void setRegSelection(Integer x){regSelection=x;} void setRegSelectionUser(String x){regSelectionUser=x;} void setPlanMoves(Integer x){planMoves=x;} void setRenewableFuel(Integer x){renewableFuel=x;} void touch(String user){sysuser=user;sysdate=LocalDateTime.now();}
    String version(){return sysdate==null?"0":sysdate.toString();}
    Map<String,Object> auditState(){var values=new LinkedHashMap<String,Object>(); values.put("sdn",sdn); values.put("registration",licencePlate); values.put("contractStart",date(startTerm)); values.put("state",stateId); values.put("cancellationDate",date(cancellationDate)); values.put("contractTerm",term); values.put("cardLastFourDigits",creditCardLastFour); values.put("creditCardRequested",creditCardRequested); values.put("creditCardExpirationDate",date(creditCardExpirationDate)); values.put("codeElement",codeElement); values.put("interiorRegime",interiorRegime); values.put("monthlyFee",monthlyFee); values.put("costCenter",costCenter); values.put("viaTCard",viaTCard); values.put("viaTCardRequested",viaTCardRequested); values.put("regSelection",regSelection); values.put("regSelectionUser",regSelectionUser); values.put("vehicleClassification",vehicleClassification); values.put("planMoves",planMoves); values.put("renewableFuel",renewableFuel); return values;}
    private static LocalDate date(LocalDateTime value){return value==null?null:value.toLocalDate();}
    CarFleetEntity duplicate(String actor){var copy=new CarFleetEntity(); copy.sdn=sdn; copy.licencePlate=licencePlate; copy.startTerm=startTerm; copy.stateId=stateId; copy.cancellationDate=cancellationDate; copy.term=term; copy.endTerm=endTerm; copy.creditCardLastFour=creditCardLastFour; copy.creditCardNumber=creditCardNumber; copy.creditCardRequested=creditCardRequested; copy.creditCardExpirationDate=creditCardExpirationDate; copy.codeElement=codeElement; copy.interiorRegime=interiorRegime; copy.monthlyFee=monthlyFee; copy.costCenter=costCenter; copy.viaTCard=viaTCard; copy.viaTCardRequested=viaTCardRequested; copy.regSelection=regSelection; copy.regSelectionUser=regSelectionUser; copy.planMoves=planMoves; copy.renewableFuel=renewableFuel; copy.vehicleClassification=vehicleClassification; copy.touch(actor); return copy;}
}

