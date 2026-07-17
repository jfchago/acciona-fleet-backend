package com.company.backend.carfleetrequests.adapters.in.rest;

import com.company.backend.carfleetrequests.application.port.in.CarFleetRequestUseCases;
import com.company.backend.carfleetrequests.application.port.in.OperationalActionResult;
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
    @GetMapping("/{id}") public ResponseEntity<ItemResponse> get(@PathVariable Long id,@RequestParam(defaultValue="ACTIVE") RequestVisibility visibility){var r=useCases.get(id,visibility);return ResponseEntity.ok().eTag(etag(r)).body(ItemResponse.from(r));}
    @PatchMapping(path="/{id}",consumes=MediaType.APPLICATION_JSON_VALUE) public ResponseEntity<UpdateResponse> update(@PathVariable Long id,@RequestHeader(HttpHeaders.IF_MATCH) String ifMatch,@RequestBody @jakarta.validation.Valid CarFleetRequestPatch changes){var result=useCases.update(id,version(ifMatch),changes.toChanges());return ResponseEntity.ok().eTag(etag(result.request())).body(new UpdateResponse(ItemResponse.from(result.request()),result.warnings()));}
    @PostMapping("/{id}/retire") public ResponseEntity<ItemResponse> retire(@PathVariable Long id,@RequestHeader(HttpHeaders.IF_MATCH) String ifMatch){var r=useCases.retire(id,version(ifMatch));return ResponseEntity.ok().eTag(etag(r)).body(ItemResponse.from(r));}
    @PostMapping("/{id}/reinstate") public ResponseEntity<ItemResponse> reinstate(@PathVariable Long id,@RequestHeader(HttpHeaders.IF_MATCH) String ifMatch){var r=useCases.reinstate(id,version(ifMatch));return ResponseEntity.ok().eTag(etag(r)).body(ItemResponse.from(r));}
    @PostMapping("/{id}/duplicate") public ResponseEntity<ItemResponse> duplicate(@PathVariable Long id){var r=useCases.duplicate(id);return ResponseEntity.status(HttpStatus.CREATED).eTag(etag(r)).body(ItemResponse.from(r));}
    @PostMapping(path="/{id}/actions", consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OperationalActionResult> action(@PathVariable Long id, @RequestBody @jakarta.validation.Valid ActionRequest request) {
        return ResponseEntity.ok(useCases.execute(id, request.action(), request.confirmed()));
    }
    private static String etag(CarFleetRequest r){return "\""+r.version()+"\"";} private static String version(String e){if(e==null||e.isBlank())throw new IllegalArgumentException("If-Match is required");if(e.startsWith("W/"))e=e.substring(2);if(!e.startsWith("\"")||!e.endsWith("\""))throw new IllegalArgumentException("If-Match must be a quoted ETag");return e.substring(1,e.length()-1);}
    public record PageResponse(List<ItemResponse> items,int page,int size,long totalElements){}
    public record UpdateResponse(ItemResponse item,List<String> warnings){}
    public record ActionRequest(@jakarta.validation.constraints.NotNull OperationalAction action, boolean confirmed){}
    public record ItemResponse(Long id,String sdn,String registration,java.time.LocalDate contractStart,Integer state,java.time.LocalDate cancellationDate,java.math.BigDecimal contractTerm,java.time.LocalDate contractEndDate,String cardLastFourDigits,boolean retired,String version,java.time.LocalDate updatedAt,String costCenter,String viaTCard,String viaTCardRequested,Integer regSelection,String regSelectionUser){static ItemResponse from(CarFleetRequest r){return new ItemResponse(r.id(),r.sdn(),r.registration(),r.contractStart(),r.state(),r.cancellationDate(),r.contractTerm(),r.contractEndDate(),r.cardLastFourDigits(),r.retired(),r.version(),r.updatedAt(),r.costCenter(),r.viaTCard(),r.viaTCardRequested(),r.regSelection(),r.regSelectionUser());}}
}
