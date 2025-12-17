package com.javaweb.controllers;

import com.javaweb.model.request.*;
import com.javaweb.model.response.ApiResponse;
import com.javaweb.model.response.LoginResponse;
import com.javaweb.model.response.RefreshTokenResponse;
import com.javaweb.model.response.UserResponse;
import com.javaweb.security.SecurityUtils;
import com.javaweb.service.AuthService;
import com.javaweb.service.PasswordResetService;
import com.javaweb.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private RegistrationService service;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse res = authService.login(request);
        return new ApiResponse<>(true, "Đăng nhập thành công", res);
    }


    @PostMapping("/refresh")
    public ApiResponse<RefreshTokenResponse> refresh(@RequestBody RefreshTokenRequest request) {
        RefreshTokenResponse res = authService.refresh(request);
        return new ApiResponse<>(true, "Làm mới token thành công", res);
    }

    @GetMapping("/test-auth")
    public String testAuth() {
        return "AUTH OK";
    }

    @PostMapping("/register/request")
    public ApiResponse<?> request(@Valid @RequestBody RegisterRequest req) {
        return service.requestRegister(req);
    }

    @PostMapping("/register/confirm")
    public ApiResponse<?> confirm(@Valid @RequestBody RegisterConfirmOtpRequest req) {
        return service.confirmRegister(req);
    }

    @DeleteMapping("/delete-account")
    public ApiResponse<?> deleteMyAccount() {
        Integer id = SecurityUtils.getPrincipal().getId();
        authService.deleteUser(id);
        return new ApiResponse<>(true, "Xóa tài khoản thành công", null);
    }

    @PostMapping("/password-reset/request")
    public ApiResponse<?> requestOtp(@Valid @RequestBody PasswordResetRequest req) {
        return passwordResetService.requestOtp(req);
    }

    @PostMapping("/password-reset/confirm")
    public ApiResponse<?> confirmOtp(@Valid @RequestBody PasswordResetConfirmRequest req) {
        return passwordResetService.confirmOtp(req);
    }

    @GetMapping("/profile")
    public ApiResponse<?> getProfile() {
        Integer userId = SecurityUtils.getPrincipal().getId();
        UserResponse user = authService.getUser(userId);

        return new ApiResponse<>(true, "success", user);
    }

    @PatchMapping("/profile")
    public ApiResponse<?> updateProfile(@RequestBody UpdateProfileRequest req) {

        Integer id = SecurityUtils.getPrincipal().getId();
        authService.updateProfile(id,req);

        return new ApiResponse<>(true, "success", null);
    }
    @PatchMapping("/change-password")
    public ApiResponse<?> changePassword(@RequestBody ChangePasswordRequest req) {

        Integer id = SecurityUtils.getPrincipal().getId();
        authService.changePassword(id, req);

        return new ApiResponse<>(true, "success", null);
    }



}