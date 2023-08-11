package org.dargor.auth.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CustomException extends RuntimeException {

    private final String timestamp = LocalDateTime.now().toString();
    private final String message;
    private final int code;

    public CustomException(String message, Integer code) {
        this.message = message;
        this.code = code == null ? 490 : code;
    }
}
