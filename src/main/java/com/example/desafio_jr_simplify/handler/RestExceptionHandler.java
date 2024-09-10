package com.example.desafio_jr_simplify.handler;

import com.example.desafio_jr_simplify.exception.BadRequestExceptionDetails;
import com.example.desafio_jr_simplify.exception.BadRequestExceptionTask;
import com.example.desafio_jr_simplify.exception.ValidationExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(BadRequestExceptionTask.class)
    public ResponseEntity<BadRequestExceptionDetails> handler(BadRequestExceptionTask bre) {
        return new ResponseEntity<>(
                BadRequestExceptionDetails.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .title("Bad Request Exception, Please check the documentation.")
                        .developerMessage(bre.getClass().getName())
                        .details(bre.getMessage())
                        .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationExceptionDetails> handler(MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        String fields = fieldErrors
                .stream()
                .map(FieldError::getField)
                .collect(Collectors.joining(", "));
        String fieldsMessage = fieldErrors
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return new ResponseEntity<>(
                ValidationExceptionDetails.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .title("Bad Request Exception, Invalid fields.")
                        .developerMessage(exception.getClass().getName())
                        .details(exception.getMessage())
                        .fields(fields)
                        .fieldsMessage(fieldsMessage)
                        .build(), HttpStatus.BAD_REQUEST);
    }
}