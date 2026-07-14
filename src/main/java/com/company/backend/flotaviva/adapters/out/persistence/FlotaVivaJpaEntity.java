package com.company.backend.flotaviva.adapters.out.persistence;

import com.company.backend.flotaviva.domain.FlotaVivaRow;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "\"V_FlotaViva\"", schema = "dbo")
class FlotaVivaJpaEntity {
    @Id private Integer id;
    @Column(name = "\"PetitionDate\"") private LocalDateTime petitionDate;
    @Column(name = "\"DivisionFiscalNumber\"") private String divisionFiscalNumber;
    @Column(name = "\"Sociedad\"") private String sociedad;
    @Column(name = "Nombre Sociedad") private String nombreSociedad;
    @Column(name = "\"DivisionGroup\"") private String divisionGroup;
    @Column(name = "\"Matricula\"") private String matricula;
    @Column(name = "\"FleetSegmentation\"") private String fleetSegmentation;
    @Column(name = "\"Marca\"") private String marca;
    @Column(name = "\"Modelo\"") private String modelo;
    @Column(name = "Descripcion Vehiculo") private String descripcionVehiculo;
    @Column(name = "Motorización") private String motorizacion;
    @Column(name = "\"Etiqueta\"") private String etiqueta;
    @Column(name = "\"CO2\"") private Integer co2;
    @Column(name = "\"Cuota\"") private BigDecimal cuota;
    @Column(name = "Estado Vehiculo") private String estadoVehiculo;
    @Column(name = "Fecha Inicio") private LocalDateTime fechaInicio;
    @Column(name = "Fecha Fin") private LocalDateTime fechaFin;
    @Column(name = "Fecha Extension") private LocalDateTime fechaExtension;
    @Column(name = "\"Proveedor\"") private String proveedor;
    @Column(name = "\"Clasificacion\"") private String clasificacion;
    @Column(name = "\"RenewableFuel\"") private Integer renewableFuel;
    @Column(name = "\"Country\"") private String country;
    @Column(name = "\"CostCenter\"") private String costCenter;
    @Column(name = "\"CodeElement\"") private String codeElement;
    @Column(name = "\"DriverName\"") private String driverName;
    @Column(name = "\"DriverMail\"") private String driverMail;
    @Column(name = "\"ResponsableVehicle\"") private String responsableVehicle;
    @Column(name = "\"eMailResponsableVehicle\"") private String emailResponsableVehicle;
    @Column(name = "\"DriverAdditionalMail\"") private String driverAdditionalMail;

    protected FlotaVivaJpaEntity() { }

    FlotaVivaRow toDomain() {
        return new FlotaVivaRow(id, petitionDate, divisionFiscalNumber, sociedad, nombreSociedad, matricula,
                fleetSegmentation, marca, modelo, descripcionVehiculo, motorizacion, etiqueta, co2, cuota,
                estadoVehiculo, fechaInicio, fechaFin, fechaExtension, proveedor, clasificacion, renewableFuel,
                costCenter, codeElement, driverName, driverMail, driverAdditionalMail, responsableVehicle,
                emailResponsableVehicle, country);
    }
}
