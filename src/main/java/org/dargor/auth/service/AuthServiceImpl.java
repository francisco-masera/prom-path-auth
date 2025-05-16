package org.dargor.auth.service;

import org.dargor.auth.dto.request.LoginRequestDto;
import org.dargor.auth.dto.request.SignUpRequestDto;
import org.dargor.auth.dto.response.UserResponseDto;
import org.dargor.auth.entity.User;
import org.dargor.auth.exception.CustomException;
import org.dargor.auth.exception.ErrorDefinition;
import org.dargor.auth.repository.AuthRepository;
import org.dargor.auth.util.JwtUtils;
import org.dargor.auth.util.UserMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final UserMapper userMapper = UserMapper.INSTANCE;
    private final AuthRepository authRepository;
    private final JwtUtils tokenUtil;


    @Override
    public UserResponseDto signUp(SignUpRequestDto request) {
        if (authRepository.getUserByEmail(request.getEmail()).isPresent()) {
            throw new CustomException(ErrorDefinition.USER_EXISTS);
        }
        User user = userMapper.signUpDtoToUser(request);
        user.setPassword(JwtUtils.encodePassword(user.getPassword()));
        User savedUser = authRepository.save(user);
        String token = tokenUtil.generateToken(request.getEmail());
        UserResponseDto response = userMapper.userToUserResponse(savedUser, token);
        log.info("User created successfully [request: {}] [response: {}]", request, response);
        return response;

    }

    @Override
    public UserResponseDto login(LoginRequestDto request) {
        User user = authRepository.getUserByEmail(request.getEmail()).orElseThrow(() -> {
            log.error("User {} NOT found!", request.getEmail());
            return new CustomException(ErrorDefinition.ENTITY_NOT_FOUND);
        });
        boolean passwordsMatches = JwtUtils.passwordMatches(user.getPassword(), request.getPassword());
        if (!passwordsMatches) {
            log.info("Passwords did not match");
            throw new CustomException(ErrorDefinition.UNAUTHORIZED);
        }
        String token = tokenUtil.generateToken(request.getEmail());
        UserResponseDto response = userMapper.userToUserResponse(user, token);
        log.info("User has successfully logged-in [request: {}] [response: {}]", request, response);
        return response;
    }

}
