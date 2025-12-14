package com.javaweb.service.impl;

import com.javaweb.entity.UserEntity;
import com.javaweb.model.request.PasswordResetConfirmRequest;
import com.javaweb.model.request.PasswordResetRequest;
import com.javaweb.model.response.ApiResponse;
import com.javaweb.repository.UserRepository;
import com.javaweb.service.EmailService;
import com.javaweb.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private StringRedisTemplate redis;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder encoder;

    private final String PREFIX = "RESET_OTP_";

    @Override
    public ApiResponse<?> requestOtp(PasswordResetRequest req) {

        if (!userRepo.existsByEmail(req.getEmail())) {
            return new ApiResponse<>(false, "Email not found", null);
        }

        String otp = String.format("%06d", new Random().nextInt(999999));

        // Lưu Redis 5 phút
        redis.opsForValue().set(PREFIX + req.getEmail(), otp, 1, TimeUnit.MINUTES);

        // Gửi mail
        emailService.sendOtp(req.getEmail(), otp);

        return new ApiResponse<>(true, "The OTP has been sent to your email", null);
    }

    @Override
    public ApiResponse<?> confirmOtp(PasswordResetConfirmRequest req) {

        String email = null;

        for (String key : redis.keys(PREFIX + "*")) { // RESET_OTP_*
            String otpSaved = redis.opsForValue().get(key);

            if (otpSaved != null && otpSaved.equals(req.getOtp())) {
                // Key dạng RESET_OTP_email
                email = key.replace(PREFIX, "");
                break;
            }
        }

        if (email == null) {
            return new ApiResponse<>(false, "The OTP is incorrect or has expired", null);
        }

        if (!req.getNewPassword().equals(req.getConfirmPassword())) {
            return new ApiResponse<>(false, "The confirmed password does not match", null);
        }

        UserEntity user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user not found"));

        user.setPassword(encoder.encode(req.getNewPassword()));
        userRepo.save(user);

        redis.delete(PREFIX + email);

        return new ApiResponse<>(true, "Password reset successfully", null);
    }


}
