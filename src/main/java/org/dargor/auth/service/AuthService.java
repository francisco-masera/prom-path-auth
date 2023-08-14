package org.dargor.auth.service;

import org.dargor.auth.dto.LoginRequestDto;
import org.dargor.auth.dto.SignUpRequestDto;
import org.dargor.auth.dto.UserResponseDto;

public interface AuthService {

    UserResponseDto signUp(SignUpRequestDto request);

    UserResponseDto login(LoginRequestDto request);


}
