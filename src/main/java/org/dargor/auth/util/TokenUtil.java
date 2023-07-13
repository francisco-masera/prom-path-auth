package org.dargor.auth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenUtil {

    private final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.expiresIn}")
    private int expiresIn;

    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    public String generateToken(String email) {
        Claims claims = Jwts.claims().setSubject(email);
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + expiresIn;
        Date exp = new Date(expMillis);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(nowMillis))
                .setExpiration(exp)
                .setIssuer(issuer)
                .signWith(SIGNATURE_ALGORITHM, jwtSecret)
                .compact();
    }

    public String encodePassword(String password) {
        return passwordEncoder().encode(password);
    }

}
