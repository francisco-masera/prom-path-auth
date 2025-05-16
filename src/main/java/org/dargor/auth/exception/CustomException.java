package org.dargor.auth.exception;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomException extends RuntimeException {

    private final String timestamp = LocalDateTime.now().toString();
    private final String message;
    private final int code;

    public CustomException(ErrorDefinition errorDefinition) {
        this.message = errorDefinition.getMessage();
        this.code = errorDefinition.getStatus().value();
    }

}
