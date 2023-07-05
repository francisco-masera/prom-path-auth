package org.dargor.auth.exception;

import lombok.Getter;

@Getter
public enum ErrorDefinition {

    INVALID_FIELDS("Please verify input data"),
    UNKNOWN_ERROR("Unknown error occurred"),
    ENTITY_NOT_FOUND("Entity not found"),
    PATH_NOT_FOUND("Path not found"),
    UNAUTHORIZED("Access denied"),
    BUSINESS_ERROR("Business error");

    private final String message;

    ErrorDefinition(String message) {
        this.message = message;
    }
}
