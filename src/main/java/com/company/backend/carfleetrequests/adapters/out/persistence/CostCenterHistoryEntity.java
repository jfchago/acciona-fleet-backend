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

