package com.company.backend.carfleetrequests.adapters.out.persistence;

import com.company.backend.carfleetrequests.application.port.out.*;
import com.company.backend.carfleetrequests.domain.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CarFleetRequestJdbcAdapter implements CarFleetRequestReadPort, CarFleetRequestWritePort, CarFleetRequestAuditPort {
    private final JdbcTemplate jdbc;
    public CarFleetRequestJdbcAdapter(JdbcTemplate jdbc) { this.jdbc=jdbc; }
    private static final String TABLE="V_CarFleetRequests";
    private static final String COLS="id,sdn,registration,contract_start,state,cancellation_date,contract_term_months,contract_end_date,card_last_four_digits,retired,version,updated_at";
    @Override public Page find(RequestVisibility visibility,int page,int size,String sort,String filter) {
        String where=(visibility==RequestVisibility.ACTIVE?" where retired=false":" where 1=1");
        String f=filter==null?"":filter;
        if(!f.isBlank()) where += " and (lower(sdn) like ? or lower(registration) like ?)";
        String order=switch(sort==null?"id":sort){case "sdn"->"sdn";case "registration"->"registration";case "contractStart"->"contract_start";case "state"->"state";case "updatedAt"->"updated_at";default->"id";};
        var params=new ArrayList<Object>(); if(!f.isBlank()){params.add("%"+f.toLowerCase(Locale.ROOT)+"%");params.add("%"+f.toLowerCase(Locale.ROOT)+"%");}
        long total=jdbc.queryForObject("select count(*) from "+TABLE+where,params.toArray(),Long.class);
        params.add(page*size); params.add(size);
        var rows=jdbc.query("select "+COLS+" from "+TABLE+where+" order by "+order+",id offset ? rows fetch next ? rows only",params.toArray(),(rs,n)->map(rs,n));
        return new Page(rows,total);
    }
    @Override public Optional<CarFleetRequest> findById(UUID id,RequestVisibility visibility) { return jdbc.query("select "+COLS+" from "+TABLE+" where id=?"+(visibility==RequestVisibility.ACTIVE?" and retired=false":""),(rs,n)->map(rs,n),id).stream().findFirst(); }
    @Override public boolean existsWithNormalizedSdn(String sdn, UUID excludingId) { return jdbc.queryForObject("select count(*) from carfleet_requests where lower(ltrim(rtrim(sdn)))=lower(ltrim(rtrim(?))) and id<>? and retired=false",Long.class,sdn,excludingId)>0; }
    @Override public Optional<CarFleetRequest> update(UUID id,long expected,Map<String,Object> changes,boolean retired) {
        var allowed=List.of("sdn","registration","contractStart","state","cancellationDate","contractTermMonths","cardLastFourDigits"); var sets=new ArrayList<String>();var args=new ArrayList<Object>();
        for(String key:allowed) if(changes.containsKey(key)){sets.add(column(key)+"=?");args.add(changes.get(key));}
        sets.add("retired=?");args.add(retired); sets.add("version=version+1"); sets.add("updated_at=?");args.add(OffsetDateTime.now(ZoneOffset.UTC)); args.add(id); args.add(expected);
        int count=jdbc.update("update carfleet_requests set "+String.join(",",sets)+" where id=? and version=?",args.toArray());
        return count==0?Optional.empty():findById(id,RequestVisibility.ALL);
    }
    @Override public void append(UUID id,String action,String actor,Map<String,Object> changes) { jdbc.update("insert into carfleet_request_audit(request_id,action,actor,changed_fields,created_at) values(?,?,?,?,?)",id,action,actor,changes==null?"{}":changes.toString(),OffsetDateTime.now(ZoneOffset.UTC)); }
    private static String column(String k){return switch(k){case "contractStart"->"contract_start";case "cancellationDate"->"cancellation_date";case "contractTermMonths"->"contract_term_months";case "cardLastFourDigits"->"card_last_four_digits";default->k;};}
    private static CarFleetRequest map(ResultSet r,int n)throws SQLException{return new CarFleetRequest((UUID)r.getObject("id"),r.getString("sdn"),r.getString("registration"),date(r,"contract_start"),intv(r,"state"),date(r,"cancellation_date"),intv(r,"contract_term_months"),date(r,"contract_end_date"),r.getString("card_last_four_digits"),r.getBoolean("retired"),r.getLong("version"),r.getObject("updated_at",OffsetDateTime.class));}
    private static LocalDate date(ResultSet r,String c)throws SQLException{var d=r.getDate(c);return d==null?null:d.toLocalDate();}
    private static Integer intv(ResultSet r,String c)throws SQLException{int v=r.getInt(c);return r.wasNull()?null:v;}
}
