package com.company.backend.flotaviva.application.port.out;

import com.company.backend.flotaviva.domain.FlotaVivaRow;
import java.util.List;

public interface FlotaVivaPersistencePort {
    Page findPage(int page, int size, String sort, String country, String filter);
    List<FlotaVivaRow> findAll(String sort, String country, String filter);

    record Page(List<FlotaVivaRow> items, long totalElements) { }
}
