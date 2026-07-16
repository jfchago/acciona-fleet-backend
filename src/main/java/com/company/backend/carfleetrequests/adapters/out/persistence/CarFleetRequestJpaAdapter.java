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
    @Column(name="InteriorRegime", length=80) private String interiorRegime;
    @Column(name="MonthlyFee", precision=19, scale=4) private BigDecimal monthlyFee;
    @Column(name="CostCenter", length=20) private String costCenter;
    @Column(name="ViaTCard", length=25) private String viaTCard;
    @Column(name="ViaTCardRequested", length=1) private String viaTCardRequested;
    @Column(name="RegSelecction") private Integer regSelection;
    @Column(name="RegSelecctionUser", length=30) private String regSelectionUser;
    @Column(name="sysuser", length=50) private String sysuser;
    @Column(name="PetitionDate") private LocalDateTime petitionDate;
    @Column(name="sysdate") private LocalDateTime sysdate;
    Long id(){return id;} String sdn(){return sdn;} String licencePlate(){return licencePlate;} Integer stateId(){return stateId;} LocalDateTime petitionDate(){return petitionDate;}
    String creditCardNumber(){return creditCardNumber;} String creditCardLastFour(){return creditCardLastFour;} LocalDate startTerm(){return date(startTerm);} BigDecimal term(){return term;}
    String codeElement(){return codeElement;} String interiorRegime(){return interiorRegime;} BigDecimal monthlyFee(){return monthlyFee;} String costCenter(){return costCenter;} String viaTCard(){return viaTCard;} String viaTCardRequested(){return viaTCardRequested;} Integer regSelection(){return regSelection;} String regSelectionUser(){return regSelectionUser;}
    void setSdn(String x){sdn=x;} void setLicencePlate(String x){licencePlate=x;} void setStartTerm(LocalDate x){startTerm=x==null?null:x.atStartOfDay();}
    void setStateId(Integer x){stateId=x;} void setCancellationDate(LocalDate x){cancellationDate=x==null?null:x.atStartOfDay();}
    void setTerm(BigDecimal x){term=x;} void setEndTerm(LocalDate x){endTerm=x==null?null:x.atStartOfDay();}
    void setCreditCardLastFour(String x){creditCardLastFour=x;} void setCodeElement(String x){codeElement=x;} void setInteriorRegime(String x){interiorRegime=x;} void setCreditCardRequested(String x){creditCardRequested=x;} void setCreditCardExpirationDate(LocalDate x){creditCardExpirationDate=x==null?null:x.atStartOfDay();}
    void setMonthlyFee(BigDecimal x){monthlyFee=x;} void setCostCenter(String x){costCenter=x;} void setViaTCard(String x){viaTCard=x;} void setViaTCardRequested(String x){viaTCardRequested=x;} void setRegSelection(Integer x){regSelection=x;} void setRegSelectionUser(String x){regSelectionUser=x;} void touch(String user){sysuser=user;sysdate=LocalDateTime.now();}
    String version(){return sysdate==null?"0":sysdate.toString();}
    Map<String,Object> auditState(){var values=new LinkedHashMap<String,Object>(); values.put("sdn",sdn); values.put("registration",licencePlate); values.put("contractStart",date(startTerm)); values.put("state",stateId); values.put("cancellationDate",date(cancellationDate)); values.put("contractTerm",term); values.put("cardLastFourDigits",creditCardLastFour); values.put("creditCardRequested",creditCardRequested); values.put("creditCardExpirationDate",date(creditCardExpirationDate)); values.put("codeElement",codeElement); values.put("interiorRegime",interiorRegime); values.put("monthlyFee",monthlyFee); values.put("costCenter",costCenter); values.put("viaTCard",viaTCard); values.put("viaTCardRequested",viaTCardRequested); values.put("regSelection",regSelection); values.put("regSelectionUser",regSelectionUser); return values;}
    private static LocalDate date(LocalDateTime value){return value==null?null:value.toLocalDate();}
    CarFleetEntity duplicate(String actor){var copy=new CarFleetEntity(); copy.sdn=sdn; copy.licencePlate=licencePlate; copy.startTerm=startTerm; copy.stateId=stateId; copy.cancellationDate=cancellationDate; copy.term=term; copy.endTerm=endTerm; copy.creditCardLastFour=creditCardLastFour; copy.creditCardNumber=creditCardNumber; copy.creditCardRequested=creditCardRequested; copy.creditCardExpirationDate=creditCardExpirationDate; copy.codeElement=codeElement; copy.interiorRegime=interiorRegime; copy.monthlyFee=monthlyFee; copy.costCenter=costCenter; copy.viaTCard=viaTCard; copy.viaTCardRequested=viaTCardRequested; copy.regSelection=regSelection; copy.regSelectionUser=regSelectionUser; copy.touch(actor); return copy;}
}

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
    @Column(name="CostCenter") private String costCenter;
    @Column(name="ViaTCard") private String viaTCard;
    @Column(name="ViaTCardRequested") private String viaTCardRequested;
    @Column(name="RegSelecction") private Integer regSelection;
    @Column(name="RegSelecctionUser") private String regSelectionUser;
    Long id(){return id;} String sdn(){return sdn;} String licencePlate(){return licencePlate;} LocalDate startTerm(){return local(startTerm);}
    Integer stateId(){return stateId;} LocalDate cancellationDate(){return local(cancellationDate);} BigDecimal term(){return term;} LocalDate endTerm(){return local(endTerm);}
    String creditCardLastFour(){return creditCardLastFour;} String country(){return country;} String costCenter(){return costCenter;} String viaTCard(){return viaTCard;} String viaTCardRequested(){return viaTCardRequested;} Integer regSelection(){return regSelection;} String regSelectionUser(){return regSelectionUser;} LocalDate updatedAt(){return local(petitionDate);}
    String version(){return petitionDate==null?"0":petitionDate.toString();}
    private static LocalDate local(LocalDateTime value){return value==null?null:value.toLocalDate();}
}

interface CarFleetRepository extends JpaRepository<CarFleetEntity, Long> {
    @org.springframework.data.jpa.repository.Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<CarFleetEntity> findForUpdateById(Long id);
}

@Entity
@Table(name="ContractHistory")
class ContractHistoryEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="CarRegistration", nullable=false, length=15) private String carRegistration;
    @Column(name="Contract", nullable=false, length=30) private String contract;
    @Column(name="InteriorRegime", length=80) private String interiorRegime;
    @Column(name="DateContract", nullable=false) private LocalDateTime dateContract;
    @Column(name="sysuser", nullable=false, length=50) private String sysuser;
    @Column(name="sysdate", nullable=false) private LocalDateTime sysdate;
    private ContractHistoryEntity(String registration, String contract, String regime, String actor, LocalDateTime now) {
        this.carRegistration=registration; this.contract=contract; this.interiorRegime=regime;
        this.dateContract=now; this.sysuser=actor; this.sysdate=now;
    }
    protected ContractHistoryEntity() { }
    static ContractHistoryEntity of(String registration, String contract, String regime, String actor) {
        return new ContractHistoryEntity(registration, contract, regime, actor, LocalDateTime.now());
    }
}

interface ContractHistoryRepository extends JpaRepository<ContractHistoryEntity, Long> { }

@Entity
@Table(name="CostCenterHistory")
class CostCenterHistoryEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="idPet") private Long petitionId;
    @Column(name="CarRegistration", length=15) private String carRegistration;
    @Column(name="CostCenter", nullable=false, length=30) private String costCenter;
    @Column(name="CostCenterDescription", length=80) private String description;
    @Column(name="DateCostCenter") private LocalDateTime dateCostCenter;
    @Column(name="sysuser", nullable=false, length=50) private String sysuser;
    @Column(name="sysdate", nullable=false) private LocalDateTime sysdate;
    protected CostCenterHistoryEntity() { }
    static CostCenterHistoryEntity of(Long petitionId,String registration,String costCenter,String actor){var x=new CostCenterHistoryEntity(); var now=LocalDateTime.now(); x.petitionId=petitionId; x.carRegistration=registration; x.costCenter=costCenter; x.dateCostCenter=now; x.sysuser=actor; x.sysdate=now; return x;}
}
interface CostCenterHistoryRepository extends JpaRepository<CostCenterHistoryEntity, Long> { }

@Entity
@Table(name="RentingFees")
class RentingFeesEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="IdCarFleet", nullable=false, precision=18, scale=4) private BigDecimal idCarFleet;
    @Column(name="Renting", precision=19, scale=4) private BigDecimal renting;
    @Column(name="InitialTerm") private LocalDateTime initialTerm;
    @Column(name="EndTerm") private LocalDateTime endTerm;
    @Column(name="UserModification", length=50) private String userModification;
    @Column(name="sysuser", nullable=false, length=50) private String sysuser;
    @Column(name="sysdate", nullable=false) private LocalDateTime sysdate;
    private RentingFeesEntity(Long requestId, BigDecimal amount, String actor, LocalDateTime now) {
        this.idCarFleet=BigDecimal.valueOf(requestId); this.renting=amount; this.initialTerm=now;
        this.userModification=actor; this.sysuser=actor; this.sysdate=now;
    }
    protected RentingFeesEntity() { }
    static RentingFeesEntity of(Long requestId, BigDecimal amount, String actor) {
        return new RentingFeesEntity(requestId, amount, actor, LocalDateTime.now());
    }
    void close(LocalDateTime at){this.endTerm=at;}
}

interface RentingFeesRepository extends JpaRepository<RentingFeesEntity, Long> {
    Optional<RentingFeesEntity> findTopByIdCarFleetAndEndTermIsNullOrderByIdDesc(BigDecimal idCarFleet);
}

@Entity
@Table(name="CreditCard")
class CreditCardEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="CarRegistration", length=15) private String carRegistration;
    @Column(name="CreditCardnumber", nullable=false, length=20) private String creditCardNumber;
    @Column(name="CreditCardLastFour", length=5) private String creditCardLastFour;
    @Column(name="RegisterDate") private LocalDateTime registerDate;
    @Column(name="CancellationDate") private LocalDateTime cancellationDate;
    @Column(name="ExpirationDate") private LocalDateTime expirationDate;
    @Column(name="CreditCardStatus", length=3) private String creditCardStatus;
    @Column(name="sysuser", nullable=false, length=50) private String sysuser;
    @Column(name="sysdate", nullable=false) private LocalDateTime sysdate;
    private CreditCardEntity(String registration, String number, String lastFour, String actor, LocalDateTime now, String status) {
        this.carRegistration=registration; this.creditCardNumber=number; this.creditCardLastFour=lastFour;
        this.registerDate="A".equals(status)?now:null; this.creditCardStatus=status; this.sysuser=actor; this.sysdate=now;
    }
    protected CreditCardEntity() { }
    static CreditCardEntity of(String registration, String number, String lastFour, String actor) {
        return new CreditCardEntity(registration, number, lastFour, actor, LocalDateTime.now(), "A");
    }
    static CreditCardEntity cancelled(String registration, String number, String lastFour, String actor) {
        var entity=new CreditCardEntity(registration,number,lastFour,actor,LocalDateTime.now(),"B"); entity.cancellationDate=entity.sysdate; return entity;
    }
    void setExpiration(LocalDate value){this.expirationDate=value==null?null:value.atStartOfDay();}
}

interface CreditCardRepository extends JpaRepository<CreditCardEntity, Long> {
    Optional<CreditCardEntity> findTopByCarRegistrationAndCreditCardStatusOrderByIdDesc(String registration, String status);
}

@Entity
@Table(name="ViaT")
class ViaTEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="ViaTnumber", length=20) private String number;
    @Column(name="CarRegistration", length=20) private String registration;
    @Column(name="ViaTStatus", length=1) private String status;
    @Column(name="DeliveredDate") private LocalDateTime deliveredDate;
    @Column(name="CancellationDate") private LocalDateTime cancellationDate;
    @Column(name="sysuser", length=50) private String sysuser;
    @Column(name="sysdate") private LocalDateTime sysdate;
    protected ViaTEntity() { }
    void activate(String actor){status="A"; deliveredDate=LocalDateTime.now(); sysuser=actor; sysdate=deliveredDate;}
    void cancel(String actor){status="B"; cancellationDate=LocalDateTime.now(); sysuser=actor; sysdate=cancellationDate;}
}
interface ViaTRepository extends JpaRepository<ViaTEntity, Long> {
    Optional<ViaTEntity> findTopByRegistrationOrderByIdDesc(String registration);
}

@Entity
@Table(name="MyVehicleAudit")
class MyVehicleAuditEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @jakarta.persistence.Column(name="idAudit") private Long id;
    @jakarta.persistence.Column(name="CodigoCampo", length=150, nullable=false) private String codigoCampo;
    @jakarta.persistence.Column(name="DelFormulario", length=40, nullable=false) private String delFormulario;
    @jakarta.persistence.Column(name="TipoCampo Nombre", length=100, nullable=false) private String tipoCampoNombre;
    @jakarta.persistence.Column(name="DatoAnterior", length=200) private String datoAnterior;
    @jakarta.persistence.Column(name="NuevoDato", length=200) private String nuevoDato;
    @jakarta.persistence.Column(name="QuienModifico", length=30) private String quienModifico;
    @jakarta.persistence.Column(name="FechaHora", nullable=false) private LocalDateTime fechaHora;
    @jakarta.persistence.Column(name="sysuser", length=50, nullable=false) private String sysuser;
    @jakarta.persistence.Column(name="sysdate", nullable=false) private LocalDateTime sysdate;
    protected MyVehicleAuditEntity() { }
    static MyVehicleAuditEntity of(String code,String form,String type,Object oldValue,Object newValue,String actor){
        var now=LocalDateTime.now(); var result=new MyVehicleAuditEntity(); result.codigoCampo=code; result.delFormulario=form; result.tipoCampoNombre=type;
        result.datoAnterior=legacyValue(oldValue); result.nuevoDato=legacyValue(newValue); result.quienModifico=actor; result.fechaHora=now; result.sysuser=actor; result.sysdate=now; return result;
    }
    private static String legacyValue(Object value){if(value==null)return ""; if(value instanceof Boolean b)return b?"Verdadero":"Falso"; return value.toString();}
}

interface MyVehicleAuditRepository extends JpaRepository<MyVehicleAuditEntity, Long> { }

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

@Component
@Transactional
public class CarFleetRequestJpaAdapter implements CarFleetRequestReadPort, CarFleetRequestWritePort, CarFleetRequestAuditPort {
    private final CarFleetRepository cars; private final CarFleetViewRepository views;
    private final ContractHistoryRepository contractHistory;
    private final RentingFeesRepository rentingFees;
    private final CreditCardRepository creditCards;
    private final MyVehicleAuditRepository audit;
    private final CostCenterHistoryRepository costCenterHistory;
    private final ViaTRepository viaT;
    public CarFleetRequestJpaAdapter(CarFleetRepository cars, CarFleetViewRepository views,
                                     ContractHistoryRepository contractHistory, RentingFeesRepository rentingFees,
                                     CreditCardRepository creditCards, MyVehicleAuditRepository audit, CostCenterHistoryRepository costCenterHistory, ViaTRepository viaT) {
        this.cars=cars; this.views=views; this.contractHistory=contractHistory;
        this.rentingFees=rentingFees; this.creditCards=creditCards; this.audit=audit; this.costCenterHistory=costCenterHistory; this.viaT=viaT;
    }
    public Page find(RequestVisibility visibility,int page,int size,String sort,String filter){
        String f=filter==null?"":filter; int offset=page*size;
        List<CarFleetViewEntity> rows=visibility==RequestVisibility.ACTIVE?views.findActive(f,offset,size):views.findAllLegacy(f,offset,size);
        long total=visibility==RequestVisibility.ACTIVE?views.countActive(f):views.countAllLegacy(f);
        return new Page(rows.stream().map(this::map).toList(),total);
    }
    public Optional<CarFleetRequest> findById(Long id,RequestVisibility visibility){return (visibility==RequestVisibility.ACTIVE?views.findActiveById(id):views.findAllLegacyById(id)).map(this::map);}
    public boolean existsWithNormalizedSdn(String sdn,Long excludingId){return views.countDuplicateSdn(sdn,excludingId)>0;}
    public Optional<CarFleetRequest> update(Long id,String expectedVersion,Map<String,Object> changes,Integer state,String actor){
        var entity=cars.findForUpdateById(id).orElse(null); if(entity==null || !version(entity).equals(expectedVersion)) return Optional.empty();
        Map<String,Object> previous=entity.auditState();
        if(changes.containsKey("sdn")) entity.setSdn((String)changes.get("sdn")); if(changes.containsKey("registration")) entity.setLicencePlate((String)changes.get("registration"));
        if(changes.containsKey("contractStart")) entity.setStartTerm(date(changes.get("contractStart"))); if(changes.containsKey("state")) entity.setStateId(state);
        if(changes.containsKey("cancellationDate")) entity.setCancellationDate(date(changes.get("cancellationDate"))); if(changes.containsKey("contractTerm")) entity.setTerm(decimal(changes.get("contractTerm")));
        if(changes.containsKey("contractStart") || changes.containsKey("contractTerm")) entity.setEndTerm(entity.startTerm()!=null && entity.term()!=null ? entity.startTerm().plusMonths(entity.term().longValue()).minusDays(1) : null);
        if(changes.containsKey("cardLastFourDigits")) entity.setCreditCardLastFour((String)changes.get("cardLastFourDigits")); if(changes.containsKey("creditCardRequested")) entity.setCreditCardRequested((String)changes.get("creditCardRequested")); if(changes.containsKey("creditCardExpirationDate")) entity.setCreditCardExpirationDate(date(changes.get("creditCardExpirationDate"))); if(changes.containsKey("codeElement")) entity.setCodeElement((String)changes.get("codeElement"));
        if(changes.containsKey("interiorRegime")) entity.setInteriorRegime((String)changes.get("interiorRegime")); if(changes.containsKey("monthlyFee")) entity.setMonthlyFee(decimal(changes.get("monthlyFee")));
        if(changes.containsKey("costCenter")) entity.setCostCenter((String)changes.get("costCenter")); if(changes.containsKey("viaTCard")) entity.setViaTCard((String)changes.get("viaTCard")); if(changes.containsKey("viaTCardRequested")) entity.setViaTCardRequested((String)changes.get("viaTCardRequested"));
        if(changes.containsKey("regSelection")) { entity.setRegSelection(integer(changes.get("regSelection"))); entity.setRegSelectionUser(entity.regSelection()!=null && entity.regSelection()==-1 ? actor : ""); }
        if(changes.containsKey("regSelectionUser")) entity.setRegSelectionUser((String)changes.get("regSelectionUser"));
        entity.touch(actor); cars.saveAndFlush(entity); persistAudit(entity, previous, entity.auditState(), changes, actor);
        persistLegacySideEffects(id, previous, entity, changes, actor);
        return findById(id,RequestVisibility.ALL);
    }
    public Optional<CarFleetRequest> duplicate(Long id,String actor){
        var source=cars.findById(id).orElse(null); if(source==null)return Optional.empty();
        var saved=cars.saveAndFlush(source.duplicate(actor)); return findById(saved.id(),RequestVisibility.ALL);
    }
    public void append(Long requestId,String action,String actor,Map<String,Object> changes){ /* update performs the transactional legacy side effects with the old snapshot */ }
    private void persistAudit(CarFleetEntity entity,Map<String,Object> previous,Map<String,Object> current,Map<String,Object> requested,String actor){
        if(requested==null) return;
        String codigo=legacyCode(entity, actor); String form="CarFleetRequests";
        requested.keySet().stream().filter(previous::containsKey).filter(k->!Objects.equals(previous.get(k),current.get(k))).forEach(k->audit.save(MyVehicleAuditEntity.of(codigo,form,controlType(k)+legacyField(k),previous.get(k),current.get(k),actor)));
    }
    private void persistLegacySideEffects(Long id,Map<String,Object> previous,CarFleetEntity entity,Map<String,Object> changes,String actor){
        LocalDateTime now=LocalDateTime.now();
        if(changes.containsKey("registration") && !Objects.equals(previous.get("registration"),entity.licencePlate()) && entity.licencePlate()!=null && !entity.licencePlate().isBlank())
            contractHistory.save(ContractHistoryEntity.of(entity.licencePlate(),entity.codeElement(),entity.interiorRegime(),actor));
        if(changes.containsKey("monthlyFee") && !Objects.equals(previous.get("monthlyFee"),entity.monthlyFee())) {
            rentingFees.findTopByIdCarFleetAndEndTermIsNullOrderByIdDesc(BigDecimal.valueOf(id)).ifPresent(x->{x.close(now); rentingFees.save(x);});
            rentingFees.save(RentingFeesEntity.of(id,entity.monthlyFee(),actor));
        }
        if(changes.containsKey("costCenter") && !Objects.equals(previous.get("costCenter"),entity.costCenter()) && entity.licencePlate()!=null && entity.costCenter()!=null)
            costCenterHistory.save(CostCenterHistoryEntity.of(id,entity.licencePlate(),entity.costCenter(),actor));
        if (entity.creditCardNumber()!=null && !entity.creditCardNumber().isBlank()) {
            String requested=string(changes.get("creditCardRequested"));
            if ("B".equalsIgnoreCase(requested)) creditCards.save(CreditCardEntity.cancelled(entity.licencePlate(),entity.creditCardNumber(),entity.creditCardLastFour(),actor));
            else if (changes.containsKey("creditCardExpirationDate") && !changes.containsKey("cardLastFourDigits") && !"A".equalsIgnoreCase(requested)) {
                creditCards.findTopByCarRegistrationAndCreditCardStatusOrderByIdDesc(entity.licencePlate(), "A")
                        .ifPresent(card -> { card.setExpiration(date(changes.get("creditCardExpirationDate"))); creditCards.save(card); });
            } else if ((changes.containsKey("cardLastFourDigits") || "A".equalsIgnoreCase(requested)) && entity.creditCardLastFour()!=null && !entity.creditCardLastFour().isBlank()) {
                creditCards.save(CreditCardEntity.of(entity.licencePlate(),entity.creditCardNumber(),entity.creditCardLastFour(),actor));
            }
        }
        if (changes.containsKey("viaTCardRequested") && entity.licencePlate()!=null)
            viaT.findTopByRegistrationOrderByIdDesc(entity.licencePlate()).ifPresent(card -> { if("A".equalsIgnoreCase(entity.viaTCardRequested())) card.activate(actor); else if("B".equalsIgnoreCase(entity.viaTCardRequested())) card.cancel(actor); viaT.save(card); });
    }
    private static String legacyCode(CarFleetEntity entity, String actor) {
        var date=entity.petitionDate()==null?LocalDateTime.now():entity.petitionDate();
        return actor + " PET" + String.format("%04d", entity.id()) + java.time.format.DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH).format(date) + date.getYear();
    }
    private static String legacyField(String key){return switch(key){case "registration"->"LicencePlate";case "contractStart"->"StartTerm";case "contractTerm"->"Term";case "state"->"StateID";case "cardLastFourDigits"->"CreditCardLastFour";case "creditCardRequested"->"CreditCardRequested";case "creditCardExpirationDate"->"CreditCardExpirationDate";case "costCenter"->"CostCenter";case "viaTCard"->"ViaTCard";case "viaTCardRequested"->"ViaTCardRequested";case "regSelection"->"RegSelecction";case "regSelectionUser"->"RegSelecctionUser";default->key;};}
    private static String controlType(String key){return Set.of("state","creditCardRequested").contains(key)?"ComboBox - ":"TextBox - ";}
    private static String string(Object value){return value==null?null:value.toString();}
    private static Integer integer(Object value){return value==null?null:value instanceof Number n?n.intValue():Integer.valueOf(value.toString());}
    private CarFleetRequest map(CarFleetViewEntity x){boolean retired=x.stateId()!=null&&(x.stateId()==14||x.stateId()==25);return new CarFleetRequest(x.id(),x.sdn(),x.licencePlate(),x.startTerm(),x.stateId(),x.cancellationDate(),x.term(),x.endTerm(),x.creditCardLastFour(),retired,version(x),x.updatedAt(),x.costCenter(),x.viaTCard(),x.viaTCardRequested(),x.regSelection(),x.regSelectionUser());}
    private String version(CarFleetViewEntity x){return x.version();} private String version(CarFleetEntity x){return x.version();}
    private static LocalDate date(Object x){return x==null?null:x instanceof LocalDate d?d:LocalDate.parse(x.toString());} private static BigDecimal decimal(Object x){return x==null?null:x instanceof BigDecimal d?d:new BigDecimal(x.toString());}
}
