package org.dargor.auth.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    @Value("${jwt-secret}")
    private String jwtSecret;
    @Value("${jwt-issuer}")
    private String issuer;
    @Value("${jwt-expiresIn}")
    private int expiresIn;

    private static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    public static String encodePassword(String password) {
        return passwordEncoder().encode(password);
    }

    public static boolean passwordMatches(String actual, String provided) {
        return passwordEncoder().matches(provided, actual);
    }

    public String generateToken(String subject) {
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + expiresIn;
        Date exp = new Date(expMillis);
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(nowMillis))
                .setExpiration(exp)
                .setIssuer(issuer)
                .signWith(SIGNATURE_ALGORITHM, jwtSecret)
                .compact();
    }

}
