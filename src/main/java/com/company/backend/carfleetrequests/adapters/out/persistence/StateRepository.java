package com.company.backend.carfleetrequests.adapters.out.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface StateRepository extends JpaRepository<StateEntity, Integer> {
    @Query("select s from StateEntity s order by s.code")
    List<StateEntity> findAllOrderedByCode();
}
