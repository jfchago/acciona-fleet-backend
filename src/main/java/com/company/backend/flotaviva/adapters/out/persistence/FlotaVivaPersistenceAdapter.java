package com.company.backend.flotaviva.adapters.out.persistence;

import com.company.backend.flotaviva.application.port.out.FlotaVivaPersistencePort;
import com.company.backend.flotaviva.domain.FlotaVivaRow;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class FlotaVivaPersistenceAdapter implements FlotaVivaPersistencePort {
    private static final Map<String, String> SORT_COLUMNS = Map.ofEntries(
            Map.entry("matricula", "Matricula"), Map.entry("id", "id"), Map.entry("petitionDate", "PetitionDate"),
            Map.entry("sociedad", "Sociedad"), Map.entry("marca", "Marca"), Map.entry("modelo", "Modelo"));
    private static final String COLUMNS = "id, PetitionDate, DivisionFiscalNumber, Sociedad, [Nombre Sociedad], Matricula, FleetSegmentation, Marca, Modelo, [Descripcion Vehiculo], [Motorización], Etiqueta, CO2, Cuota, [Estado Vehiculo], [Fecha Inicio], [Fecha Fin], [Fecha Extension], Proveedor, Clasificacion, RenewableFuel, CostCenter, CodeElement, DriverName, DriverMail, DriverAdditionalMail, ResponsableVehicle, eMailResponsableVehicle, Country";
    private final NamedParameterJdbcTemplate jdbc;

    public FlotaVivaPersistenceAdapter(NamedParameterJdbcTemplate jdbc) { this.jdbc = jdbc; }

    @Override
    public Page findPage(int page, int size, String sort, String country, String filter) {
        var params = params(country, filter);
        String order = orderBy(sort);
        String sql = "SELECT " + COLUMNS + " FROM dbo.V_FlotaViva WHERE Country = :country" + filterClause() + " ORDER BY " + order + " OFFSET :offset ROWS FETCH NEXT :size ROWS ONLY";
        params.addValue("offset", page * size).addValue("size", size);
        return new Page(jdbc.query(sql, params, (rs, n) -> row(rs)), count(params));
    }

    @Override
    public List<FlotaVivaRow> findAll(String sort, String country, String filter) {
        var params = params(country, filter);
        return jdbc.query("SELECT " + COLUMNS + " FROM dbo.V_FlotaViva WHERE Country = :country" + filterClause() + " ORDER BY " + orderBy(sort), params, (rs, n) -> row(rs));
    }

    private static String orderBy(String sort) {
        String column = SORT_COLUMNS.getOrDefault(sort == null ? "matricula" : sort, "Matricula");
        return "id".equals(column) ? column : column + ", id";
    }

    private long count(MapSqlParameterSource params) { return jdbc.queryForObject("SELECT COUNT_BIG(*) FROM dbo.V_FlotaViva WHERE Country = :country" + filterClause(), params, Long.class); }
    private static String filterClause() { return " AND (:filter = '' OR " + "LOWER(CAST(DivisionFiscalNumber AS varchar(max))) LIKE LOWER(:likeFilter) OR LOWER(CAST(Sociedad AS varchar(max))) LIKE LOWER(:likeFilter) OR LOWER(CAST([Nombre Sociedad] AS varchar(max))) LIKE LOWER(:likeFilter) OR LOWER(CAST(Marca AS varchar(max))) LIKE LOWER(:likeFilter) OR LOWER(CAST(Modelo AS varchar(max))) LIKE LOWER(:likeFilter) OR LOWER(CAST([Descripcion Vehiculo] AS varchar(max))) LIKE LOWER(:likeFilter) OR LOWER(CAST([Motorización] AS varchar(max))) LIKE LOWER(:likeFilter) OR LOWER(CAST(Etiqueta AS varchar(max))) LIKE LOWER(:likeFilter) OR LOWER(CAST(CO2 AS varchar(max))) LIKE LOWER(:likeFilter) OR LOWER(CAST(Proveedor AS varchar(max))) LIKE LOWER(:likeFilter) OR LOWER(CAST(Clasificacion AS varchar(max))) LIKE LOWER(:likeFilter) OR LOWER(CAST(DivisionGroup AS varchar(max))) LIKE LOWER(:likeFilter) OR LOWER(CAST(Matricula AS varchar(max))) LIKE LOWER(:likeFilter))"; }
    private static MapSqlParameterSource params(String country, String filter) { return new MapSqlParameterSource(Map.of("country", country, "filter", filter, "likeFilter", "%" + filter + "%")); }
    private static FlotaVivaRow row(java.sql.ResultSet rs) throws java.sql.SQLException { return new FlotaVivaRow(rs.getObject(1), rs.getObject(2), rs.getObject(3), rs.getObject(4), rs.getObject(5), rs.getObject(6), rs.getObject(7), rs.getObject(8), rs.getObject(9), rs.getObject(10), rs.getObject(11), rs.getObject(12), rs.getObject(13), rs.getObject(14), rs.getObject(15), rs.getObject(16), rs.getObject(17), rs.getObject(18), rs.getObject(19), rs.getObject(20), rs.getObject(21), rs.getObject(22), rs.getObject(23), rs.getObject(24), rs.getObject(25), rs.getObject(26), rs.getObject(27), rs.getObject(28), rs.getObject(29)); }
}
