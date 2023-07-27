package org.dargor.auth.controller;

import lombok.AllArgsConstructor;
import org.dargor.auth.dto.LoginRequestDto;
import org.dargor.auth.dto.SignUpRequestDto;
import org.dargor.auth.dto.TokenResponseDto;
import org.dargor.auth.dto.UserResponseDto;
import org.dargor.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/sign-up", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UserResponseDto> signUp(@RequestBody @Valid SignUpRequestDto request) {
        var response = authService.signUp(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping(value = "/login", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UserResponseDto> login(@RequestBody @Valid LoginRequestDto request) {
        var response = authService.login(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/b2b-token", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<TokenResponseDto> getB2BToken() {
        var response = authService.getB2BToken();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
