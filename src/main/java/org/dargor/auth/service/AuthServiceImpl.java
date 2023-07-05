package org.dargor.auth.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dargor.auth.dto.LoginDto;
import org.dargor.auth.dto.SignUpDto;
import org.dargor.auth.dto.UserResponseDto;
import org.dargor.auth.exception.ErrorDefinition;
import org.dargor.auth.repository.AuthRepository;
import org.dargor.auth.util.CustomerMapper;
import org.dargor.auth.util.TokenUtil;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final AuthRepository authRepository;
    private final CustomerMapper customerMapper = CustomerMapper.INSTANCE;

    private final TokenUtil tokenUtil;

    @Override
    public UserResponseDto login(LoginDto request) {
        var customer = authRepository.findByEmailAndPassword(request.getEmail(), request.getPassword()).orElseThrow(() -> {
            log.error(String.format("User NOT %s found!", request.getEmail()));
            return new UsernameNotFoundException(ErrorDefinition.ENTITY_NOT_FOUND.getMessage());
        });
        String token = tokenUtil.generateToken(request.getEmail());
        var response = customerMapper.customerToUserResponse(customer, token);
        log.info(String.format("Customer logged in successfully [request: %s] [response: %s]", request, response));
        return response;
    }

    @Override
    public UserResponseDto signup(SignUpDto request) {
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info(String.format("Looking for user %s", username));
        var customer = authRepository.findByEmail(username).orElseThrow(() -> {
            log.error(String.format("User NOT %s found!", username));
            return new UsernameNotFoundException(ErrorDefinition.ENTITY_NOT_FOUND.getMessage());
        });
        var user = new org.springframework.security.core.userdetails.User(
                username,
                customer.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        log.info(String.format("User %s found!", username));
        return user;
    }
}
