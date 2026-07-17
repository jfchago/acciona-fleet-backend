package com.company.backend.carfleetrequests.adapters.out.persistence;

import com.company.backend.carfleetrequests.domain.VehicleClassification;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "VehicleClasification")
class VehicleClasificationEntity {
    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "VehicleClasification")
    private String name;

    @Column(name = "Country")
    private String country;

    VehicleClassification toDomain() {
        return new VehicleClassification(id, name);
    }
}
