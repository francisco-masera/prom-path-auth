package org.dargor.auth.service;

import org.dargor.auth.dto.request.LoginRequestDto;
import org.dargor.auth.dto.request.SignUpRequestDto;
import org.dargor.auth.dto.response.UserResponseDto;

public interface AuthService {

    UserResponseDto signUp(SignUpRequestDto request);

    UserResponseDto login(LoginRequestDto request);


}
