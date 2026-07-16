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

