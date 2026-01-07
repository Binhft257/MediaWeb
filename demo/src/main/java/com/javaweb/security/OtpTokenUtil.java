package com.javaweb.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class OtpTokenUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    public String generateOtpToken(String email, int otp) {
        return Jwts.builder()
                .claim("email", email)
                .claim("otp", otp)
                .setExpiration(new Date(System.currentTimeMillis() + 5 * 60 * 1000)) // 5 phút
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }
}
