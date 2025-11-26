package com.javaweb.service.impl;

import com.javaweb.entity.RefreshTokenEntity;
import com.javaweb.model.request.ChangePasswordRequest;
import com.javaweb.model.request.UpdateProfileRequest;
import com.javaweb.entity.UserEntity;
import com.javaweb.model.request.LoginRequest;
import com.javaweb.model.request.RefreshTokenRequest;
import com.javaweb.model.response.LoginResponse;
import com.javaweb.model.response.RefreshTokenResponse;
import com.javaweb.model.response.UserResponse;
import com.javaweb.repository.RoleRepository;
import com.javaweb.repository.UserRepository;
import com.javaweb.security.JwtTokenUtil;
import com.javaweb.service.AuthService;
import com.javaweb.service.TokenService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JwtTokenUtil jwtUtil;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserEntity user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not found"));

        if ("inactive".equals(user.getStatus())) {
            throw new RuntimeException("Account has been deleted");
        }

        String access = jwtUtil.generateAccessToken(user);

        RefreshTokenEntity refresh = tokenService.createRefreshToken(user);

        UserResponse userRes = new UserResponse();
        userRes.setId(user.getId());
        userRes.setName(user.getName());
        userRes.setEmail(user.getEmail());
        userRes.setRole(user.getRole().getName());
        userRes.setStatus(user.getStatus());
        userRes.setUserGender(user.getUserGender());
        userRes.setUserDob(user.getUserDob());
        userRes.setAvatar(user.getAvatar());

        return new LoginResponse(access, refresh.getToken(), userRes);
    }

    @Override
    public RefreshTokenResponse refresh(RefreshTokenRequest request) {

        RefreshTokenEntity oldToken = tokenService.verify(request.getRefreshToken());

        UserEntity user = oldToken.getUser();

        if ("inactive".equals(user.getStatus())) {
            throw new RuntimeException("Account has been deleted");
        }

        RefreshTokenEntity newToken = tokenService.createRefreshToken(user);

        String accessToken = jwtUtil.generateAccessToken(user);

        return new RefreshTokenResponse(accessToken, newToken.getToken());
    }


    @Override
    @Transactional
    public void deleteUser(Integer id) {
        UserEntity user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User does not exist"));

        user.setStatus("inactive");

        String hiddenEmail = user.getEmail() + "#deleted#" + System.currentTimeMillis();
        user.setEmail(hiddenEmail);

        userRepo.save(user);

    }

    @Override
    public UserResponse getUser(Integer id) {
        UserEntity user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        userResponse.setRole(user.getRole().getName());
        return userResponse;

    }

    @Override
    public void updateProfile(Integer id, UpdateProfileRequest request) {
        UserEntity user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        modelMapper.map(request, user); // Update từng field (null không ghi đè)

        userRepo.save(user);
    }

    @Override
    public void changePassword(Integer id, ChangePasswordRequest request) {
        UserEntity user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("The old password is incorrect");
        }

        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new RuntimeException("The new password must not be the same as the old password");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("The re-entered new password does not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepo.save(user);
    }
}
