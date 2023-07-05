package org.dargor.auth.util;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class TokenUtil {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER = "Bearer";
    private final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.expiresIn}")
    private int expiresIn;

    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    public void validateToken(final String token) throws MalformedJwtException, UnsupportedJwtException {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
        } catch (SignatureException ex) {
            throw new MalformedJwtException("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            throw new MalformedJwtException("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new MalformedJwtException("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new MalformedJwtException("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new UnsupportedJwtException("JWT claims string is empty.");
        }
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
                .signWith(SIGNATURE_ALGORITHM, secret)
                .compact();
    }

    public String getToken(HttpServletRequest request) {
        var token = request.getHeader(AUTHORIZATION_HEADER);
        if (token != null && token.startsWith(BEARER + " ")) {
            return token.substring(token.lastIndexOf(BEARER + " "));
        }
        return null;
    }

    public String encodePassword(String password) {
        return passwordEncoder().encode(password);
    }

}
