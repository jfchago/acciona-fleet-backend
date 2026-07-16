package com.company.backend.carfleetrequests.application.service;

import com.company.backend.carfleetrequests.domain.RequestValidation;
import java.util.List;
import java.util.UUID;

public final class CarFleetRequestExceptions {
    private CarFleetRequestExceptions() { }
    public static class NotFound extends RuntimeException { public NotFound(UUID id) { super("CarFleetRequest not found: " + id); } }
    public static class Forbidden extends RuntimeException { public Forbidden() { super("Action is not authorized"); } }
    public static class Conflict extends RuntimeException { public Conflict() { super("The request was modified by another client"); } }
    public static class Invalid extends RuntimeException { private final List<RequestValidation.Violation> violations; public Invalid(List<RequestValidation.Violation> v) { super("CarFleetRequest validation failed"); violations=v; } public List<RequestValidation.Violation> violations(){return violations;} }
    public static class DuplicateSdn extends RuntimeException { public DuplicateSdn() { super("A request with the same SDN already exists"); } }
}
