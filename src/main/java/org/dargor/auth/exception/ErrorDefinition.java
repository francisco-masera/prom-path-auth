package org.dargor.auth.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorDefinition {

    INVALID_FIELDS("Please verify input data", HttpStatus.BAD_REQUEST),
    UNKNOWN_ERROR("Unknown error occurred", HttpStatus.INTERNAL_SERVER_ERROR),
    ENTITY_NOT_FOUND("Entity not found", HttpStatus.NOT_FOUND),
    UNAUTHORIZED("Access denied", HttpStatus.UNAUTHORIZED),
    USER_EXISTS("User already exists", HttpStatus.UNPROCESSABLE_ENTITY);

    private final String message;

    private final HttpStatus status;

    ErrorDefinition(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
