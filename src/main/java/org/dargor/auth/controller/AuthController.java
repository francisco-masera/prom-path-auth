package org.dargor.auth.controller;

import lombok.AllArgsConstructor;
import org.dargor.auth.dto.LoginRequestDto;
import org.dargor.auth.dto.SignUpRequestDto;
import org.dargor.auth.dto.UserResponseDto;
import org.dargor.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<UserResponseDto> signUp(@RequestBody SignUpRequestDto request) {
        var response = authService.signUp(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody LoginRequestDto request) {
        var response = authService.login(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
