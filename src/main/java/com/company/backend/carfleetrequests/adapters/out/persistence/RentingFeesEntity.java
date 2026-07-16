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

