package org.dargor.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dargor.auth.dto.LoginRequestDto;
import org.dargor.auth.dto.SignUpRequestDto;
import org.dargor.auth.dto.TokenResponseDto;
import org.dargor.auth.dto.UserResponseDto;
import org.dargor.auth.exception.CustomException;
import org.dargor.auth.exception.ErrorDefinition;
import org.dargor.auth.repository.AuthRepository;
import org.dargor.auth.util.JwtUtils;
import org.dargor.auth.util.TokenMapper;
import org.dargor.auth.util.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final UserMapper userMapper = UserMapper.INSTANCE;
    private static final TokenMapper tokenMapper = TokenMapper.INSTANCE;
    private final AuthRepository authRepository;
    private final JwtUtils tokenUtil;

    @Value("${jwt.b2b.subject}")
    private String b2bSubject;

    @Override
    public UserResponseDto signUp(SignUpRequestDto request) {
        if (authRepository.findUserByEmail(request.getEmail()).isPresent())
            throw new CustomException(ErrorDefinition.USER_EXISTS.getMessage(), null);
        var user = userMapper.signUpDtoToUser(request);
        user.setPassword(JwtUtils.encodePassword(user.getPassword()));
        var savedUser = authRepository.save(user);
        String token = tokenUtil.generateToken(request.getEmail());
        var response = userMapper.userToUserResponse(savedUser, token);
        log.info(String.format("User created successfully [request: %s] [response: %s]", request, response));
        return response;

    }

    @Override
    public UserResponseDto login(LoginRequestDto request) {
        var user = authRepository.getUserByEmail(request.getEmail()).orElseThrow(() -> {
            log.error(String.format("User %s NOT found!", request.getEmail()));
            return new CustomException(ErrorDefinition.ENTITY_NOT_FOUND.getMessage(), null);
        });
        var passwordsMatches = JwtUtils.passwordMatches(user.getPassword(), request.getPassword());
        if (!passwordsMatches) {
            log.info("Passwords did not match");
            throw new CustomException(ErrorDefinition.UNAUTHORIZED.getMessage(), null);
        }
        String token = tokenUtil.generateToken(request.getEmail());
        var response = userMapper.userToUserResponse(user, token);
        log.info(String.format("User has successfully logged-in [request: %s] [response: %s]", request, response));
        return response;
    }

    @Override
    public TokenResponseDto getB2BToken() {
        var newToken = tokenUtil.generateToken(b2bSubject);
        var response = tokenMapper.signUpDtoToUser(newToken);
        log.info("Token granted successfully [request: %s] [response: %s]");
        return response;
    }

}
