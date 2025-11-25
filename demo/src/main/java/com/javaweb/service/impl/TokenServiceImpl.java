package com.javaweb.service.impl;

import com.javaweb.entity.RefreshTokenEntity;
import com.javaweb.entity.UserEntity;
import com.javaweb.repository.RefreshTokenRepository;
import com.javaweb.service.TokenService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private RefreshTokenRepository tokenRepo;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration; // milliseconds

    @Transactional
    @Override
    public RefreshTokenEntity createRefreshToken(UserEntity user) {

        tokenRepo.deleteByUser(user);

        RefreshTokenEntity token = new RefreshTokenEntity();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(new Date(System.currentTimeMillis() + refreshExpiration));
        token.setRevoked(false);

        return tokenRepo.save(token);
    }

    @Override
    public RefreshTokenEntity verify(String tokenString) {

        RefreshTokenEntity token = tokenRepo.findByToken(tokenString)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (token.isRevoked()) {
            throw new RuntimeException("Refresh token is revoked");
        }

        if (token.getExpiryDate().before(new Date())) {
            throw new RuntimeException("Refresh token expired");
        }

        return token;
    }
}
