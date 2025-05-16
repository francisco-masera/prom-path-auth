package org.dargor.auth.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ExceptionAdviser {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<ErrorResponse> argsNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
        ErrorResponse error = new ErrorResponse(String.format("%s: %s", ErrorDefinition.INVALID_FIELDS.getMessage(), errors),
              HttpStatus.BAD_REQUEST.value());
        log.error("Exception found with code {} for field validation didn't passed.", error.getCode());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomException.class)
    public final ResponseEntity<ErrorResponse> customException(CustomException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), ex.getCode());
        log.error("Exception found with code {}.", error.getCode());
        return new ResponseEntity<>(error, null, error.getCode());
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> genericError() {
        ErrorResponse error = new ErrorResponse(ErrorDefinition.UNKNOWN_ERROR.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        log.error("Exception found with code {}.", error.getCode());
        return new ResponseEntity<>(error, null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
