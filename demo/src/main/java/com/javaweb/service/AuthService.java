package com.javaweb.service;

import com.javaweb.model.request.LoginRequest;
import com.javaweb.model.request.RefreshTokenRequest;
import com.javaweb.model.request.RegisterRequest;
import com.javaweb.model.response.LoginResponse;
import com.javaweb.model.response.RefreshTokenResponse;
import com.javaweb.model.response.UserResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    RefreshTokenResponse refresh(RefreshTokenRequest request);
    void deleteUser(Integer id);
}
