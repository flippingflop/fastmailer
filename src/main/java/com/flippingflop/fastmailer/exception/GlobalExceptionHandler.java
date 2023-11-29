package com.flippingflop.fastmailer.exception;

import com.flippingflop.fastmailer.exception.model.CustomValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<?> handleCustomValidationException(CustomValidationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
