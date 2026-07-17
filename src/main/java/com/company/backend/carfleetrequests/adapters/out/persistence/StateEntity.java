package com.company.backend.carfleetrequests.adapters.out.persistence;

import com.company.backend.carfleetrequests.domain.State;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "State")
class StateEntity {
    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "StateCode")
    private String code;

    @Column(name = "StateDescription")
    private String description;

    State toDomain() {
        return new State(id, code, description);
    }
}
