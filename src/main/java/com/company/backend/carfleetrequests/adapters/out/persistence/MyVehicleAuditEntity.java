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

