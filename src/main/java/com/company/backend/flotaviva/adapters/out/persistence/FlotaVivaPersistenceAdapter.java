package com.company.backend.flotaviva.adapters.out.persistence;

import com.company.backend.flotaviva.application.port.out.FlotaVivaPersistencePort;
import com.company.backend.flotaviva.domain.FlotaVivaRow;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class FlotaVivaPersistenceAdapter implements FlotaVivaPersistencePort {
    private static final Map<String, String> SORT_PROPERTIES = Map.of(
            "matricula", "matricula", "id", "id", "petitionDate", "petitionDate",
            "sociedad", "sociedad", "marca", "marca", "modelo", "modelo");

    private final FlotaVivaSpringDataRepository repository;

    public FlotaVivaPersistenceAdapter(FlotaVivaSpringDataRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page findPage(int page, int size, String sort, String country, String filter) {
        var result = repository.findPage(country, filter, pageRequest(page, size, sort));
        return new Page(result.getContent().stream().map(FlotaVivaJpaEntity::toDomain).toList(), result.getTotalElements());
    }

    @Override
    public List<FlotaVivaRow> findAll(String sort, String country, String filter) {
        return repository.findAll(country, filter, sortBy(sort)).stream().map(FlotaVivaJpaEntity::toDomain).toList();
    }

    private static PageRequest pageRequest(int page, int size, String sort) {
        return PageRequest.of(page, size, sortBy(sort));
    }

    private static Sort sortBy(String sort) {
        String property = SORT_PROPERTIES.getOrDefault(sort == null ? "matricula" : sort, "matricula");
        return Sort.by(Sort.Order.asc(property), Sort.Order.asc("id"));
    }
}
