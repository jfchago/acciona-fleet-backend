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

