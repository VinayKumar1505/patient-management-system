package com.pm.auth_service.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    private final SecretKey secretKey;

    public JwtUtil(@Value("${jwt.secret}") String secretKey) {
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        this.secretKey = Keys.hmacShaKeyFor(decodedKey);
    }

    public String generateToken(String email, String role) {
        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    //validate the token
    public void validateToken(String token) {
        //try catch signature and jwt exception
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
        } catch (SignatureException e) {
            throw new SignatureException("Invalid JWT token signature");
        } catch (JwtException e) {
            throw new JwtException("Invalid JWT token");
        }
    }
}
