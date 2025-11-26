package com.javaweb.service.impl;

import com.javaweb.entity.RoleEntity;
import com.javaweb.entity.UserEntity;
import com.javaweb.model.request.RegisterConfirmOtpRequest;
import com.javaweb.model.request.RegisterRequest;
import com.javaweb.model.response.ApiResponse;
import com.javaweb.repository.RoleRepository;
import com.javaweb.repository.UserRepository;
import com.javaweb.service.EmailService;
import com.javaweb.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    @Autowired private UserRepository userRepo;
    @Autowired private StringRedisTemplate redis;
    @Autowired private EmailService emailService;
    @Autowired private PasswordEncoder encoder;
    @Autowired private RoleRepository roleRepo;

    private final String DATA_PREFIX = "REGISTER_DATA_";
    private final String OTP_PREFIX = "REGISTER_OTP_";
    private final String OTP_EMAIL_PREFIX = "REGISTER_OTP_EMAIL_";

    @Override
    public ApiResponse<?> requestRegister(RegisterRequest req) {

        if (userRepo.existsByEmail(req.getEmail())) {
            return new ApiResponse<>(false, "Email already exists", null);
        }

        if (!req.getPassword().equals(req.getConfirmPassword())) {
            return new ApiResponse<>(false, "The confirmed password does not match", null);
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonUser = mapper.writeValueAsString(req);

            redis.opsForValue().set(
                    DATA_PREFIX + req.getEmail(),
                    jsonUser,
                    5,
                    TimeUnit.MINUTES
            );
        } catch (Exception e) {
            return new ApiResponse<>(false, "System error while saving data", null);
        }

        String otp = String.format("%06d", new Random().nextInt(999999));

        redis.opsForValue().set(OTP_PREFIX + req.getEmail(), otp, 5, TimeUnit.MINUTES);
        redis.opsForValue().set(OTP_EMAIL_PREFIX + otp, req.getEmail(), 5, TimeUnit.MINUTES);

        emailService.sendOtp(req.getEmail(), otp);

        return new ApiResponse<>(true, "Verification OTP has been sent to your email", null);
    }

    @Override
    public ApiResponse<?> confirmRegister(RegisterConfirmOtpRequest req) {

        String email = redis.opsForValue().get(OTP_EMAIL_PREFIX + req.getOtp());
        if (email == null) {
            return new ApiResponse<>(false, "The OTP is invalid or has expired", null);
        }

        String jsonData = redis.opsForValue().get(DATA_PREFIX + email);
        if (jsonData == null) {
            return new ApiResponse<>(false, "Your registration information has expired, please register again", null);
        }

        RegisterRequest data;
        try {
            ObjectMapper mapper = new ObjectMapper();
            data = mapper.readValue(jsonData, RegisterRequest.class);
        } catch (Exception e) {
            return new ApiResponse<>(false, "Error reading registration data", null);
        }

        RoleEntity role = roleRepo.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Role USER not found"));

        UserEntity user = new UserEntity();
        user.setEmail(data.getEmail());
        user.setName(data.getName());
        user.setUserGender(data.getGender());
        user.setUserDob(data.getUserDob());
        user.setPassword(encoder.encode(data.getPassword()));
        user.setStatus("active");
        user.setAvatar(data.getAvatar());
        user.setRole(role);


        userRepo.save(user);

        redis.delete(DATA_PREFIX + email);
        redis.delete(OTP_PREFIX + email);
        redis.delete(OTP_EMAIL_PREFIX + req.getOtp());

        return new ApiResponse<>(true, "success", null);
    }
}
