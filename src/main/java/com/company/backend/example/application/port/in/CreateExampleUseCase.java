package com.company.backend.example.application.port.in;

import com.company.backend.example.domain.ExampleRecord;

public interface CreateExampleUseCase {

    ExampleRecord create(String value);
}
