package org.dargor.auth.controller;

import lombok.AllArgsConstructor;
import org.dargor.auth.dto.LoginDto;
import org.dargor.auth.dto.SignUpDto;
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


    @PostMapping("/get-token")
    public ResponseEntity<UserResponseDto> login(@RequestBody LoginDto request) {
        var response = authService.login(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserResponseDto> signup(@RequestBody SignUpDto request) {
        var response = authService.signup(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


}
