package com.company.backend.shared.adapters.in.rest.error;

import com.company.backend.example.application.service.ExampleNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.validation.ConstraintViolationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import com.company.backend.carfleetrequests.application.service.CarFleetRequestExceptions;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentTypeMismatchException.class,
            IllegalArgumentException.class})
    ProblemDetail handleBadRequest(Exception exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        problem.setTitle("Invalid request parameters");
        return problem;
    }

    @ExceptionHandler(ExampleNotFoundException.class)
    ProblemDetail handleNotFound(ExampleNotFoundException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problem.setTitle("Example not found");
        return problem;
    }

    @ExceptionHandler(CarFleetRequestExceptions.NotFound.class)
    ProblemDetail requestNotFound(CarFleetRequestExceptions.NotFound e) { return problem(HttpStatus.NOT_FOUND,"CarFleetRequest not found",e.getMessage()); }
    @ExceptionHandler(CarFleetRequestExceptions.Forbidden.class)
    ProblemDetail forbidden(CarFleetRequestExceptions.Forbidden e) { return problem(HttpStatus.FORBIDDEN,"Action not authorized",e.getMessage()); }
    @ExceptionHandler(CarFleetRequestExceptions.Conflict.class)
    ProblemDetail conflict(CarFleetRequestExceptions.Conflict e) { return problem(HttpStatus.CONFLICT,"Concurrent modification",e.getMessage()); }
    @ExceptionHandler(CarFleetRequestExceptions.Invalid.class)
    ProblemDetail invalid(CarFleetRequestExceptions.Invalid e) { var p=problem(HttpStatus.UNPROCESSABLE_ENTITY,"Validation failed",e.getMessage()); p.setProperty("errors",e.violations()); return p; }
    private static ProblemDetail problem(HttpStatus s,String title,String detail){var p=ProblemDetail.forStatusAndDetail(s,detail);p.setTitle(title);return p;}

    @ExceptionHandler(CarFleetRequestExceptions.DuplicateSdn.class)
    ProblemDetail duplicate(CarFleetRequestExceptions.DuplicateSdn e) { return problem(HttpStatus.UNPROCESSABLE_ENTITY,"Duplicate SDN",e.getMessage()); }
}
