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

@Component
@Transactional
public class CarFleetRequestJpaAdapter implements CarFleetRequestReadPort, CarFleetRequestWritePort, CarFleetRequestAuditPort, CarFleetRequestMasterDataPort {
    private final CarFleetRepository cars; private final CarFleetViewRepository views;
    private final ContractHistoryRepository contractHistory;
    private final RentingFeesRepository rentingFees;
    private final CreditCardRepository creditCards;
    private final MyVehicleAuditRepository audit;
    private final CostCenterHistoryRepository costCenterHistory;
    private final ViaTRepository viaT;
    private final StateRepository states;
    private final VehicleClasificationRepository vehicleClasifications;
    public CarFleetRequestJpaAdapter(CarFleetRepository cars, CarFleetViewRepository views,
                                     ContractHistoryRepository contractHistory, RentingFeesRepository rentingFees,
                                     CreditCardRepository creditCards, MyVehicleAuditRepository audit, CostCenterHistoryRepository costCenterHistory, ViaTRepository viaT,
                                     StateRepository states, VehicleClasificationRepository vehicleClasifications) {
        this.cars=cars; this.views=views; this.contractHistory=contractHistory;
        this.rentingFees=rentingFees; this.creditCards=creditCards; this.audit=audit; this.costCenterHistory=costCenterHistory; this.viaT=viaT;
        this.states = states; this.vehicleClasifications = vehicleClasifications;
    }
    @Override public List<State> findStates(){return states.findAllOrderedByCode().stream().map(StateEntity::toDomain).toList();}
    @Override public List<VehicleClassification> findVehicleClassificationsForSpain(){return vehicleClasifications.findSpanishOrderedByName().stream().map(VehicleClasificationEntity::toDomain).toList();}
    public Page find(RequestVisibility visibility,int page,int size,String sort,String filter){
        String f=filter==null?"":filter; int offset=page*size;
        List<CarFleetViewEntity> rows=visibility==RequestVisibility.ACTIVE?views.findActive(f,offset,size):views.findAllLegacy(f,offset,size);
        long total=visibility==RequestVisibility.ACTIVE?views.countActive(f):views.countAllLegacy(f);
        return new Page(rows.stream().map(this::map).toList(),total);
    }
    public Optional<CarFleetRequest> findById(Long id,RequestVisibility visibility){
        var row = visibility == RequestVisibility.ACTIVE
                ? views.findActiveById(id)
                : views.findAllLegacyById(id).or(() -> views.findActiveById(id));
        return row.map(this::map);
    }
    public boolean existsWithNormalizedSdn(String sdn,Long excludingId){return views.countDuplicateSdn(sdn,excludingId)>0;}
    public Optional<CarFleetRequest> update(Long id,String expectedVersion,Map<String,Object> changes,Integer state,String actor){
        var entity=cars.findForUpdateById(id).orElse(null); if(entity==null || !version(entity).equals(expectedVersion)) return Optional.empty();
        Map<String,Object> previous=entity.auditState();
        if(changes.containsKey("sdn")) entity.setSdn((String)changes.get("sdn")); if(changes.containsKey("registration")) entity.setLicencePlate((String)changes.get("registration"));
        if(changes.containsKey("contractStart")) entity.setStartTerm(date(changes.get("contractStart"))); if(changes.containsKey("state")) entity.setStateId(state);
        if(changes.containsKey("vehicleClassification")) entity.setVehicleClassification((String) changes.get("vehicleClassification"));
        if(changes.containsKey("cancellationDate")) entity.setCancellationDate(date(changes.get("cancellationDate"))); if(changes.containsKey("contractTerm")) entity.setTerm(decimal(changes.get("contractTerm")));
        if(changes.containsKey("contractStart") || changes.containsKey("contractTerm")) entity.setEndTerm(entity.startTerm()!=null && entity.term()!=null ? entity.startTerm().plusMonths(entity.term().longValue()).minusDays(1) : null);
        if(changes.containsKey("cardLastFourDigits")) entity.setCreditCardLastFour((String)changes.get("cardLastFourDigits")); if(changes.containsKey("creditCardRequested")) entity.setCreditCardRequested((String)changes.get("creditCardRequested")); if(changes.containsKey("creditCardExpirationDate")) entity.setCreditCardExpirationDate(date(changes.get("creditCardExpirationDate"))); if(changes.containsKey("codeElement")) entity.setCodeElement((String)changes.get("codeElement"));
        if(changes.containsKey("interiorRegime")) entity.setInteriorRegime((String)changes.get("interiorRegime")); if(changes.containsKey("monthlyFee")) entity.setMonthlyFee(decimal(changes.get("monthlyFee")));
        if(changes.containsKey("costCenter")) entity.setCostCenter((String)changes.get("costCenter")); if(changes.containsKey("viaTCard")) entity.setViaTCard((String)changes.get("viaTCard")); if(changes.containsKey("viaTCardRequested")) entity.setViaTCardRequested((String)changes.get("viaTCardRequested"));
        if(changes.containsKey("regSelection")) { entity.setRegSelection(integer(changes.get("regSelection"))); entity.setRegSelectionUser(entity.regSelection()!=null && entity.regSelection()==-1 ? actor : ""); }
        if(changes.containsKey("regSelectionUser")) entity.setRegSelectionUser((String)changes.get("regSelectionUser"));
        entity.touch(actor); cars.saveAndFlush(entity); persistAudit(entity, previous, entity.auditState(), changes, actor);
        persistLegacySideEffects(id, previous, entity, changes, actor);
        return findByIdAny(id);
    }
    public Optional<CarFleetRequest> duplicate(Long id,String actor){
        var source=cars.findById(id).orElse(null); if(source==null)return Optional.empty();
        var saved=cars.saveAndFlush(source.duplicate(actor)); return findByIdAny(saved.id());
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
    private static String legacyField(String key){return switch(key){case "registration"->"LicencePlate";case "contractStart"->"StartTerm";case "contractTerm"->"Term";case "state"->"StateID";case "vehicleClassification"->"VehicleClasification";case "cardLastFourDigits"->"CreditCardLastFour";case "creditCardRequested"->"CreditCardRequested";case "creditCardExpirationDate"->"CreditCardExpirationDate";case "costCenter"->"CostCenter";case "viaTCard"->"ViaTCard";case "viaTCardRequested"->"ViaTCardRequested";case "regSelection"->"RegSelecction";case "regSelectionUser"->"RegSelecctionUser";default->key;};}
    private static String controlType(String key){return Set.of("state","creditCardRequested","vehicleClassification").contains(key)?"ComboBox - ":"TextBox - ";}
    private static String string(Object value){return value==null?null:value.toString();}
    private static Integer integer(Object value){return value==null?null:value instanceof Number n?n.intValue():Integer.valueOf(value.toString());}
    private Optional<CarFleetRequest> findByIdAny(Long id){return views.findActiveById(id).or(() -> views.findAllLegacyById(id)).map(this::map);}
    private CarFleetRequest map(CarFleetViewEntity x){boolean retired=x.stateId()!=null&&(x.stateId()==14||x.stateId()==25);return new CarFleetRequest(x.id(),x.sdn(),x.licencePlate(),x.startTerm(),x.stateId(),x.cancellationDate(),x.term(),x.endTerm(),x.creditCardLastFour(),retired,version(x),x.updatedAt(),x.costCenter(),x.viaTCard(),x.viaTCardRequested(),x.regSelection(),x.regSelectionUser(),x.petitionId(),x.divisionName(),x.substitutionVehicle(),x.driverName(),x.director(),x.stateCode(),x.stateDescription(),x.monthlyFee(),x.contract(),x.provider(),x.vehicleClassification(),x.fuelType(),x.co2Index(),x.environmentalTag(),x.documentation(),x.planMoves(),x.renewableFuel(),x.country());}
    private String version(CarFleetViewEntity x){return x.version();} private String version(CarFleetEntity x){return x.version();}
    private static LocalDate date(Object x){return x==null?null:x instanceof LocalDate d?d:LocalDate.parse(x.toString());} private static BigDecimal decimal(Object x){return x==null?null:x instanceof BigDecimal d?d:new BigDecimal(x.toString());}
}

