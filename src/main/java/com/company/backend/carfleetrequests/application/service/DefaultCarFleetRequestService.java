package com.company.backend.carfleetrequests.application.service;

import com.company.backend.carfleetrequests.application.port.in.CarFleetRequestUseCases;
import com.company.backend.carfleetrequests.application.port.out.*;
import com.company.backend.carfleetrequests.domain.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class DefaultCarFleetRequestService implements CarFleetRequestUseCases {
    private static final Set<String> EDITABLE = Set.of("sdn", "registration", "contractStart", "state", "cancellationDate", "contractTerm", "cardLastFourDigits", "creditCardRequested", "creditCardExpirationDate", "codeElement", "interiorRegime", "monthlyFee", "regSelection", "regSelectionUser", "costCenter", "viaTCard", "viaTCardRequested");
    private final CarFleetRequestReadPort reads; private final CarFleetRequestWritePort writes; private final CarFleetRequestAuditPort audits;
    private final CurrentUserPort users; private final CarFleetRequestAuthorizationPort authorization;
    public DefaultCarFleetRequestService(CarFleetRequestReadPort reads, CarFleetRequestWritePort writes, CarFleetRequestAuditPort audits,
                                         CurrentUserPort users, CarFleetRequestAuthorizationPort authorization) {
        this.reads=reads; this.writes=writes; this.audits=audits; this.users=users; this.authorization=authorization;
    }
    @Override @Transactional(readOnly=true)
    public Page list(RequestVisibility visibility,int page,int size,String sort,String filter) {
        require(null,CarFleetRequestAuthorizationPort.Action.READ);
        if (visibility==null) visibility=RequestVisibility.ACTIVE;
        if (page<0 || size<1 || size>500) throw new IllegalArgumentException("Invalid pagination");
        if (sort!=null && !Set.of("id","sdn","registration","contractStart","state","updatedAt").contains(sort)) throw new IllegalArgumentException("Unsupported sort");
        if (filter!=null && filter.length()>100) throw new IllegalArgumentException("filter must not exceed 100 characters");
        var p=reads.find(visibility,page,size,sort,filter==null?"":filter.trim()); return new Page(p.items(),page,size,p.totalElements());
    }
    @Override @Transactional(readOnly=true)
    public CarFleetRequest get(Long id,RequestVisibility visibility) { require(id,CarFleetRequestAuthorizationPort.Action.READ); return reads.findById(id,visibility==null?RequestVisibility.ACTIVE:visibility).orElseThrow(()->new CarFleetRequestExceptions.NotFound(id)); }
    @Override
    public UpdateResult update(Long id,String version,Map<String,Object> changes) {
        var user=require(id,CarFleetRequestAuthorizationPort.Action.UPDATE);
        var effective=new LinkedHashMap<>(changes==null?Map.of():changes);
        if (effective.containsKey("regSelection")) {
            Integer selected=integer(effective.get("regSelection"));
            effective.put("regSelectionUser", selected != null && selected == -1 ? user.id() : "");
        }
        var warnings=new ArrayList<String>();
        if (effective.containsKey("sdn") && reads.existsWithNormalizedSdn((String)effective.get("sdn"),id)) warnings.add("SDN_DUPLICATE: Este SDN ya ha sido utilizado anteriormente; revise el valor antes de continuar.");
        var saved=mutate(id,version,effective,CarFleetRequestAuthorizationPort.Action.UPDATE,"UPDATE",user);
        return new UpdateResult(saved,warnings);
    }
    @Override public CarFleetRequest retire(Long id,String version) { return mutate(id,version,Map.of("state", CarFleetRequest.CANCELLED_STATE),CarFleetRequestAuthorizationPort.Action.RETIRE,"RETIRE"); }
    @Override public CarFleetRequest reinstate(Long id,String version) { return mutate(id,version,Map.of("state", CarFleetRequest.ACTIVE_STATE),CarFleetRequestAuthorizationPort.Action.REINSTATE,"REINSTATE"); }
    @Override public CarFleetRequest duplicate(Long id) {
        var user=require(id,CarFleetRequestAuthorizationPort.Action.DUPLICATE);
        return writes.duplicate(id,user.id()).orElseThrow(()->new CarFleetRequestExceptions.NotFound(id));
    }
    private CarFleetRequest mutate(Long id,String version,Map<String,Object> changes,CarFleetRequestAuthorizationPort.Action action,String auditAction) { return mutate(id,version,changes,action,auditAction,require(id,action)); }
    private CarFleetRequest mutate(Long id,String version,Map<String,Object> changes,CarFleetRequestAuthorizationPort.Action action,String auditAction,CurrentUserPort.User user) {
        var current=reads.findById(id,RequestVisibility.ALL).orElseThrow(()->new CarFleetRequestExceptions.NotFound(id));
        if (version==null || !version.equals(current.version())) throw new CarFleetRequestExceptions.Conflict();
        if (action==CarFleetRequestAuthorizationPort.Action.UPDATE) {
            if (changes==null || changes.isEmpty() || !EDITABLE.containsAll(changes.keySet())) throw new IllegalArgumentException("Only editable request fields may be changed");
            if ("A".equalsIgnoreCase(String.valueOf(changes.get("creditCardRequested")))) {
                var digits=changes.containsKey("cardLastFourDigits")?changes.get("cardLastFourDigits"):current.cardLastFourDigits();
                if (digits==null || digits.toString().isBlank()) throw new CarFleetRequestExceptions.Invalid(List.of(new RequestValidation.Violation("cardLastFourDigits", "required when activating a credit card")));
            }
            var candidate=merge(current,changes); var violations=RequestValidation.validate(candidate); if(!violations.isEmpty()) throw new CarFleetRequestExceptions.Invalid(violations);
            if ("A".equalsIgnoreCase(string(changes.get("creditCardRequested"))) && (candidate.cardLastFourDigits()==null || candidate.cardLastFourDigits().isBlank()))
                throw new CarFleetRequestExceptions.Invalid(List.of(new RequestValidation.Violation("creditCardRequested", "E: introducir los últimos dígitos de la tarjeta antes de activar")));
        }
        var saved=writes.update(id,version,changes,integer(changes.getOrDefault("state", current.state())),user.id()).orElseThrow(CarFleetRequestExceptions.Conflict::new);
        audits.append(id,auditAction,user.id(),changes); return saved;
    }
    private CurrentUserPort.User require(Long id,CarFleetRequestAuthorizationPort.Action action) { var u=users.current(); if(!authorization.allowed(u,action,id)) throw new CarFleetRequestExceptions.Forbidden(); return u; }
    private static CarFleetRequest merge(CarFleetRequest c,Map<String,Object> x) {
        String sdn=(String)x.getOrDefault("sdn",c.sdn()), reg=(String)x.getOrDefault("registration",c.registration()), digits=(String)x.getOrDefault("cardLastFourDigits",c.cardLastFourDigits());
        LocalDate start=date(x.getOrDefault("contractStart",c.contractStart())), cancel=date(x.getOrDefault("cancellationDate",c.cancellationDate()));
        Integer state=integer(x.getOrDefault("state",c.state())); BigDecimal term=decimal(x.getOrDefault("contractTerm",c.contractTerm()));
        LocalDate end=start!=null&&term!=null&&term.signum()>0?start.plusMonths(term.longValueExact()).minusDays(1):c.contractEndDate();
        return new CarFleetRequest(c.id(),sdn,reg,start,state,cancel,term,end,digits,c.retired(),c.version(),c.updatedAt(),c.costCenter(),c.viaTCard(),c.viaTCardRequested(),c.regSelection(),c.regSelectionUser());
    }
    private static LocalDate date(Object o){return o==null?null:o instanceof LocalDate d?d:LocalDate.parse(o.toString());}
    private static Integer integer(Object o){return o==null?null:o instanceof Number n?n.intValue():Integer.valueOf(o.toString());}
    private static BigDecimal decimal(Object o){return o==null?null:o instanceof BigDecimal d?d:o instanceof Number n?BigDecimal.valueOf(n.doubleValue()):new BigDecimal(o.toString());}
    private static String string(Object o){return o==null?null:o.toString();}
}
