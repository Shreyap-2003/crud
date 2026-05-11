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

        return createProblemDetail( HttpStatus.NOT_FOUND, ex.getMessage() );
    }

    @ExceptionHandler(BadRequestException.class)
    public ProblemDetail handleBadRequest( BadRequestException ex ) {

        return createProblemDetail( HttpStatus.BAD_REQUEST, ex.getMessage() );
    }

    @ExceptionHandler(BusinessValidationException.class)
    public ProblemDetail handleBusinessValidation( BusinessValidationException ex )
    {
        return createProblemDetail( HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage() );
    }

    private ProblemDetail createProblemDetail( HttpStatus status, String message)
    {
        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail( status, message );

        problemDetail.setProperty( "timestamp", LocalDateTime.now() );

        return problemDetail;
    }
}