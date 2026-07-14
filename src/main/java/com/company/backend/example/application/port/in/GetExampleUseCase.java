package com.company.backend.example.application.port.in;

import com.company.backend.example.domain.ExampleRecord;
import java.util.UUID;

public interface GetExampleUseCase {

    ExampleRecord get(UUID id);
}
