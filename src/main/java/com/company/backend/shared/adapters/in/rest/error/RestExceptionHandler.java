package com.company.backend.shared.adapters.in.rest.error;

import com.company.backend.example.application.service.ExampleNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ExampleNotFoundException.class)
    ProblemDetail handleNotFound(ExampleNotFoundException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problem.setTitle("Example not found");
        return problem;
    }
}
