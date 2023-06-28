package com.demo.productservice.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        final Map<String, Object> body = new HashMap<>();
        body.put("message", "Unique constraint violation");
        
        if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
            body.put("message", "Duplicate entry.");
        }
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
       final Map<String, Object> body = new HashMap<>();
        body.put("message", "Validation failed");
        body.put("errors", ex.getBindingResult().getAllErrors());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
