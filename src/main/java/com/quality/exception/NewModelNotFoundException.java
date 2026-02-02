package com.quality.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.lang.NonNull;
import org.springframework.web.ErrorResponseException;

import java.net.URI;
import java.util.Objects;

public class NewModelNotFoundException extends ErrorResponseException {

    public NewModelNotFoundException(String message){
        super(HttpStatus.NOT_FOUND, asProblemDetail(message), null);
    }

    @NonNull
    private static ProblemDetail asProblemDetail(String message){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND, 
            Objects.requireNonNull(message, "Error message cannot be null")
        );
        problemDetail.setTitle("Model Not Found");
        problemDetail.setType(Objects.requireNonNull(URI.create("/not-found")));
        problemDetail.setProperty("new value", "value test");
        problemDetail.setProperty("age", 32);
        return problemDetail;
    }
}
