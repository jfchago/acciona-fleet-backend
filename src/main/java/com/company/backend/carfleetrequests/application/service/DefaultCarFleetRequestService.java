package com.company.backend.carfleetrequests.application.service;

import com.company.backend.carfleetrequests.application.port.in.CarFleetRequestUseCases;
import com.company.backend.carfleetrequests.application.port.out.*;
import com.company.backend.carfleetrequests.domain.*;
import java.time.LocalDate;
import java.util.*;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class DefaultCarFleetRequestService implements CarFleetRequestUseCases {
    private static final Set<String> EDITABLE = Set.of("sdn", "registration", "contractStart", "state", "cancellationDate", "contractTermMonths", "cardLastFourDigits");
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
    public CarFleetRequest get(UUID id,RequestVisibility visibility) { require(id,CarFleetRequestAuthorizationPort.Action.READ); return reads.findById(id,visibility==null?RequestVisibility.ACTIVE:visibility).orElseThrow(()->new CarFleetRequestExceptions.NotFound(id)); }
    @Override
    public CarFleetRequest update(UUID id,long version,Map<String,Object> changes) { return mutate(id,version,changes,CarFleetRequestAuthorizationPort.Action.UPDATE,"UPDATE"); }
    @Override public CarFleetRequest retire(UUID id,long version) { return mutate(id,version,Map.of("retired",true),CarFleetRequestAuthorizationPort.Action.RETIRE,"RETIRE"); }
    @Override public CarFleetRequest reinstate(UUID id,long version) { return mutate(id,version,Map.of("retired",false),CarFleetRequestAuthorizationPort.Action.REINSTATE,"REINSTATE"); }
    private CarFleetRequest mutate(UUID id,long version,Map<String,Object> changes,CarFleetRequestAuthorizationPort.Action action,String auditAction) {
        var user=require(id,action); var current=reads.findById(id,RequestVisibility.ALL).orElseThrow(()->new CarFleetRequestExceptions.NotFound(id));
        if (version<0 || version!=current.version()) throw new CarFleetRequestExceptions.Conflict();
        if (action==CarFleetRequestAuthorizationPort.Action.UPDATE) {
            if (changes==null || changes.isEmpty() || !EDITABLE.containsAll(changes.keySet())) throw new IllegalArgumentException("Only editable request fields may be changed");
            var candidate=merge(current,changes); var violations=RequestValidation.validate(candidate); if(!violations.isEmpty()) throw new CarFleetRequestExceptions.Invalid(violations);
            if (candidate.sdn()!=null && reads.existsWithNormalizedSdn(candidate.sdn(),id)) throw new CarFleetRequestExceptions.DuplicateSdn();
        }
        var saved=writes.update(id,version,changes,action==CarFleetRequestAuthorizationPort.Action.RETIRE ? true : action==CarFleetRequestAuthorizationPort.Action.REINSTATE ? false : current.retired()).orElseThrow(CarFleetRequestExceptions.Conflict::new);
        audits.append(id,auditAction,user.id(),changes); return saved;
    }
    private CurrentUserPort.User require(UUID id,CarFleetRequestAuthorizationPort.Action action) { var u=users.current(); if(!authorization.allowed(u,action,id)) throw new CarFleetRequestExceptions.Forbidden(); return u; }
    private static CarFleetRequest merge(CarFleetRequest c,Map<String,Object> x) {
        String sdn=(String)x.getOrDefault("sdn",c.sdn()), reg=(String)x.getOrDefault("registration",c.registration()), digits=(String)x.getOrDefault("cardLastFourDigits",c.cardLastFourDigits());
        LocalDate start=date(x.getOrDefault("contractStart",c.contractStart())), cancel=date(x.getOrDefault("cancellationDate",c.cancellationDate()));
        Integer state=integer(x.getOrDefault("state",c.state())), term=integer(x.getOrDefault("contractTermMonths",c.contractTermMonths()));
        LocalDate end=start!=null&&term!=null&&term>0?start.plusMonths(term):c.contractEndDate();
        return new CarFleetRequest(c.id(),sdn,reg,start,state,cancel,term,end,digits,c.retired(),c.version(),c.updatedAt());
    }
    private static LocalDate date(Object o){return o==null?null:o instanceof LocalDate d?d:LocalDate.parse(o.toString());}
    private static Integer integer(Object o){return o==null?null:o instanceof Number n?n.intValue():Integer.valueOf(o.toString());}
}
