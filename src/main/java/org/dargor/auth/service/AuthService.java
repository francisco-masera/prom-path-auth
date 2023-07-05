package org.dargor.auth.service;

import org.dargor.auth.dto.LoginDto;
import org.dargor.auth.dto.SignUpDto;
import org.dargor.auth.dto.UserResponseDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {

    UserResponseDto login(LoginDto request);

    UserResponseDto signup(SignUpDto request);

}
