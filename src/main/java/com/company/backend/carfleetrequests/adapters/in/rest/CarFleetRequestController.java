package com.company.backend.carfleetrequests.adapters.in.rest;

import com.company.backend.carfleetrequests.application.port.in.CarFleetRequestUseCases;
import com.company.backend.carfleetrequests.domain.*;
import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.*;

@RestController
@RequestMapping(path="/api/v1/car-fleet-requests",produces=MediaType.APPLICATION_JSON_VALUE)
public class CarFleetRequestController {
    private final CarFleetRequestUseCases useCases;
    public CarFleetRequestController(CarFleetRequestUseCases useCases){this.useCases=useCases;}
    @GetMapping public ResponseEntity<PageResponse> list(@RequestParam(defaultValue="ACTIVE") RequestVisibility visibility,@RequestParam(defaultValue="0") @Min(0) int page,@RequestParam(defaultValue="50") @Min(1) @Max(500) int size,@RequestParam(defaultValue="id") String sort,@RequestParam(defaultValue="") @Size(max=100) String filter){var p=useCases.list(visibility,page,size,sort,filter);return ResponseEntity.ok(new PageResponse(p.items().stream().map(ItemResponse::from).toList(),p.page(),p.size(),p.totalElements()));}
    @GetMapping("/{id}") public ResponseEntity<ItemResponse> get(@PathVariable UUID id,@RequestParam(defaultValue="ACTIVE") RequestVisibility visibility){var r=useCases.get(id,visibility);return ResponseEntity.ok().eTag(etag(r)).body(ItemResponse.from(r));}
    @PatchMapping(path="/{id}",consumes=MediaType.APPLICATION_JSON_VALUE) public ResponseEntity<ItemResponse> update(@PathVariable UUID id,@RequestHeader(HttpHeaders.IF_MATCH) String ifMatch,@RequestBody Map<String,Object> changes){var r=useCases.update(id,version(ifMatch),changes);return ResponseEntity.ok().eTag(etag(r)).body(ItemResponse.from(r));}
    @PostMapping("/{id}/retire") public ResponseEntity<ItemResponse> retire(@PathVariable UUID id,@RequestHeader(HttpHeaders.IF_MATCH) String ifMatch){var r=useCases.retire(id,version(ifMatch));return ResponseEntity.ok().eTag(etag(r)).body(ItemResponse.from(r));}
    @PostMapping("/{id}/reinstate") public ResponseEntity<ItemResponse> reinstate(@PathVariable UUID id,@RequestHeader(HttpHeaders.IF_MATCH) String ifMatch){var r=useCases.reinstate(id,version(ifMatch));return ResponseEntity.ok().eTag(etag(r)).body(ItemResponse.from(r));}
    private static String etag(CarFleetRequest r){return "\""+r.version()+"\"";} private static long version(String e){if(e==null)throw new IllegalArgumentException("If-Match is required");try{return Long.parseLong(e.replace("\"",""));}catch(NumberFormatException x){throw new IllegalArgumentException("If-Match must contain a numeric ETag");}}
    public record PageResponse(List<ItemResponse> items,int page,int size,long totalElements){}
    public record ItemResponse(UUID id,String sdn,String registration,java.time.LocalDate contractStart,Integer state,java.time.LocalDate cancellationDate,Integer contractTermMonths,java.time.LocalDate contractEndDate,String cardLastFourDigits,boolean retired,long version,java.time.OffsetDateTime updatedAt){static ItemResponse from(CarFleetRequest r){return new ItemResponse(r.id(),r.sdn(),r.registration(),r.contractStart(),r.state(),r.cancellationDate(),r.contractTermMonths(),r.contractEndDate(),r.cardLastFourDigits(),r.retired(),r.version(),r.updatedAt());}}
}
