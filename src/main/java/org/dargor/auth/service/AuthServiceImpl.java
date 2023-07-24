package org.dargor.auth.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dargor.auth.dto.LoginRequestDto;
import org.dargor.auth.dto.SignUpRequestDto;
import org.dargor.auth.dto.UserResponseDto;
import org.dargor.auth.exception.ErrorDefinition;
import org.dargor.auth.repository.AuthRepository;
import org.dargor.auth.util.TokenUtil;
import org.dargor.auth.util.UserMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@Slf4j
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final AuthRepository authRepository;
    private final UserMapper userMapper = UserMapper.INSTANCE;

    private final TokenUtil tokenUtil;

    @Override
    public UserResponseDto signUp(SignUpRequestDto request) {
        try {
            var user = userMapper.signUpDtoToUser(request);
            user.setPassword(tokenUtil.encodePassword(user.getPassword()));
            var savedUser = authRepository.save(user);
            String token = tokenUtil.generateToken(request.getEmail());
            var response = userMapper.userToUserResponse(savedUser, token);
            log.info(String.format("User created successfully [request: %s] [response: %s]", request, response));
            return response;
        } catch (Exception e) {
            log.error(String.format("Error found creating user [request: %s] [error: %s]", request.toString(), e.getMessage()));
            throw e;
        }
    }

    @Override
    public UserResponseDto login(LoginRequestDto request) {
        var user = authRepository.findByEmailAndPassword(request.getEmail(), tokenUtil.encodePassword(request.getPassword())).orElseThrow(() -> {
            log.error(String.format("User %s NOT found!", request.getEmail()));
            return new EntityNotFoundException(ErrorDefinition.ENTITY_NOT_FOUND.getMessage());
        });
        String token = tokenUtil.generateToken(request.getEmail());
        var response = userMapper.userToUserResponse(user, token);
        log.info(String.format("User logged in successfully [request: %s] [response: %s]", request, response));
        return response;
    }

}
