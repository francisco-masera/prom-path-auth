package org.dargor.auth.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExceptionAdviser {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<ErrorMessageResponse> argsNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
        var errorMessage = new ErrorMessageResponse(String.format("%s: %s", ErrorDefinition.INVALID_FIELDS.getMessage(), errors), HttpStatus.BAD_REQUEST.value());
        log.error(String.format("Exception found with code %s for field validation didn't passed.", errorMessage.getCode()));
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EntityNotFoundException.class, UsernameNotFoundException.class})
    public final ResponseEntity<ErrorMessageResponse> entityNotFound() {
        var errorMessage = new ErrorMessageResponse(ErrorDefinition.ENTITY_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND.value());
        log.error("Entity not found");
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<ErrorMessageResponse> accessDenied(AccessDeniedException e) {
        var errorMessage = new ErrorMessageResponse(ErrorDefinition.UNAUTHORIZED.getMessage(), HttpStatus.UNAUTHORIZED.value());
        log.error(String.format("Exception found with code %d.", errorMessage.getCode()));
        return new ResponseEntity<>(errorMessage, null, errorMessage.getCode());
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorMessageResponse> genericError(Exception e) {
        var errorMessage = new ErrorMessageResponse(ErrorDefinition.UNKNOWN_ERROR.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        log.error(String.format("Exception found with code %d.", errorMessage.getCode()));
        return new ResponseEntity<>(errorMessage, null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
