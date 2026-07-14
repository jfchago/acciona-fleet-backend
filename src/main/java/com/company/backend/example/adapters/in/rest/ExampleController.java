package com.company.backend.example.adapters.in.rest;

import com.company.backend.example.application.port.in.CreateExampleUseCase;
import com.company.backend.example.application.port.in.GetExampleUseCase;
import com.company.backend.example.domain.ExampleRecord;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/examples", produces = MediaType.APPLICATION_JSON_VALUE)
public class ExampleController {

    private final CreateExampleUseCase createExampleUseCase;
    private final GetExampleUseCase getExampleUseCase;

    public ExampleController(CreateExampleUseCase createExampleUseCase, GetExampleUseCase getExampleUseCase) {
        this.createExampleUseCase = createExampleUseCase;
        this.getExampleUseCase = getExampleUseCase;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ExampleResponse create(@Valid @RequestBody CreateExampleRequest request) {
        return ExampleResponse.from(createExampleUseCase.create(request.value()));
    }

    @GetMapping("/{id}")
    public ExampleResponse get(@PathVariable UUID id) {
        return ExampleResponse.from(getExampleUseCase.get(id));
    }

    public record CreateExampleRequest(@NotBlank @Size(max = 255) String value) {
    }

    public record ExampleResponse(UUID id, String value, OffsetDateTime createdAt) {

        static ExampleResponse from(ExampleRecord example) {
            return new ExampleResponse(example.id(), example.value(), example.createdAt());
        }
    }
}
