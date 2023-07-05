package org.dargor.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.dargor.auth.dto.SignUpDto;
import org.dargor.auth.exception.ErrorDefinition;
import org.dargor.auth.service.AuthService;
import org.dargor.auth.util.CustomerMapper;
import org.dargor.auth.util.TokenUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
@AllArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenUtil tokenUtil;
    private AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = tokenUtil.getToken(request);
        tokenUtil.validateToken(token);
        final CustomerMapper customerMapper = CustomerMapper.INSTANCE;
        try {
            var signUpDto = getSignUpBody(request);
            var customer = customerMapper.signUpDtoToCustomer(signUpDto);
            var userDetails = authService.loadUserByUsername(customer.getEmail());
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (IOException e) {
            throw new AccessDeniedException(ErrorDefinition.UNAUTHORIZED.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private SignUpDto getSignUpBody(HttpServletRequest request) throws IOException {
        InputStream inputStream = request.getInputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        byte[] requestBody = outputStream.toByteArray();
        return new ObjectMapper().readValue(requestBody, SignUpDto.class);
    }
}
