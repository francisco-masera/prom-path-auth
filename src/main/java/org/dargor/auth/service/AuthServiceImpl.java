package org.dargor.auth.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dargor.auth.dto.LoginRequestDto;
import org.dargor.auth.dto.SignUpRequestDto;
import org.dargor.auth.dto.UserResponseDto;
import org.dargor.auth.exception.ErrorDefinition;
import org.dargor.auth.repository.AuthRepository;
import org.dargor.auth.util.CustomerMapper;
import org.dargor.auth.util.TokenUtil;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@Slf4j
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final AuthRepository authRepository;
    private final CustomerMapper customerMapper = CustomerMapper.INSTANCE;

    private final TokenUtil tokenUtil;

    @Override
    public UserResponseDto signup(SignUpRequestDto request) {
        try {
            var customer = customerMapper.signUpDtoToCustomer(request);
            customer.setPassword(tokenUtil.encodePassword(customer.getPassword()));
            var savedCustomer = authRepository.save(customer);
            String token = tokenUtil.generateToken(request.getEmail());
            var response = customerMapper.customerToUserResponse(savedCustomer, token);
            log.info(String.format("Customer created successfully [request: %s] [response: %s]", request, response));
            return response;
        } catch (Exception e) {
            log.error(String.format("Error found creating customer [request: %s] [error: %s]", request.toString(), e.getMessage()));
            throw e;
        }
    }

    @Override
    public UserResponseDto login(LoginRequestDto request) {
        var customer = authRepository.findByEmailAndPassword(request.getEmail(), request.getPassword()).orElseThrow(() -> {
            log.error(String.format("User NOT %s found!", request.getEmail()));
            return new EntityNotFoundException(ErrorDefinition.ENTITY_NOT_FOUND.getMessage());
        });
        String token = tokenUtil.generateToken(request.getEmail());
        var response = customerMapper.customerToUserResponse(customer, token);
        log.info(String.format("Customer logged in successfully [request: %s] [response: %s]", request, response));
        return response;
    }

}
