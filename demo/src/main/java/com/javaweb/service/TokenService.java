package com.javaweb.service;

import com.javaweb.entity.RefreshTokenEntity;
import com.javaweb.entity.UserEntity;

public interface TokenService {
    RefreshTokenEntity createRefreshToken(UserEntity user);
    RefreshTokenEntity verify(String tokenString);

}
