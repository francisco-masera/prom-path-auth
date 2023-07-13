package org.dargor.auth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {

    private final String message;
    private final int code;
    private final String timestamp = LocalDateTime.now().toString();

}
