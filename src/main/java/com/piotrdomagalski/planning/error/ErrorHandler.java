package com.piotrdomagalski.planning.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Controller advice class for Spring- global exception handler to map errors for helpful response.
 * reach method returns ResponseEntity.
 */

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorEntity<Map<String, List<String>>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, List<String>> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            List<String> params = errors.getOrDefault(fieldName, new ArrayList<>());
            params.add(errorMessage);
            errors.put(fieldName, params);
        });
        return new ResponseEntity<>(new ErrorEntity<>(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                errors
        ), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = NoSuchElementException.class)
    ResponseEntity<ErrorEntity<String>> handleNoSuchElementException(NoSuchElementException e) {
        return new ResponseEntity<>(new ErrorEntity<>(HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = IllegalOperationException.class)
    ResponseEntity<ErrorEntity<String>> handleIllegalOperationException(IllegalOperationException e) {
        return new ResponseEntity<>(new ErrorEntity<>(HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = DateTimeParseException.class)
    ResponseEntity<ErrorEntity<String>> handleDateTimeParseException(DateTimeParseException e) {
        return new ResponseEntity<>(new ErrorEntity<>(HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Incorrect date provided:" + e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}