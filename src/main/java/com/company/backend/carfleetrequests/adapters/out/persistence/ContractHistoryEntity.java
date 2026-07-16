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

