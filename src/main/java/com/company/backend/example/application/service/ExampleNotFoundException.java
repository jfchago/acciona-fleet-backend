package com.company.backend.example.application.service;

import java.util.UUID;

public class ExampleNotFoundException extends RuntimeException {

    public ExampleNotFoundException(UUID id) {
        super("Example with id " + id + " was not found");
    }
}
