package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound( ResourceNotFoundException ex ) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail( HttpStatus.NOT_FOUND,
                ex.getMessage() );

        addTimestamp(problemDetail);

        return problemDetail;
    }

    @ExceptionHandler(BadRequestException.class)
    public ProblemDetail handleBadRequest( BadRequestException ex) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, ex.getMessage());

        addTimestamp(problemDetail);

        return problemDetail;
    }

    @ExceptionHandler(BusinessValidationException.class)
    public ProblemDetail handleBusinessValidation(BusinessValidationException ex) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage() );

        addTimestamp(problemDetail);

        return problemDetail;
    }

    private void addTimestamp( ProblemDetail problemDetail )
    {
        problemDetail.setProperty( "timestamp", LocalDateTime.now() );
    }
}