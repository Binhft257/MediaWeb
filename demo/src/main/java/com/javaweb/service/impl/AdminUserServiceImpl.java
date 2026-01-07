package com.javaweb.service.impl;

import com.javaweb.entity.RoleEntity;
import com.javaweb.entity.UserEntity;
import com.javaweb.model.response.UserResponse;
import com.javaweb.repository.RoleRepository;
import com.javaweb.repository.UserRepository;
import com.javaweb.service.AdminUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AdminUserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    private RoleEntity roleOrThrow(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
    }

    private UserResponse toResponse(UserEntity u) {
        UserResponse r = new UserResponse();
        r.setId(u.getId());
        r.setName(u.getName());
        r.setEmail(u.getEmail());
        r.setStatus(u.getStatus());
        r.setUserGender(u.getUserGender());
        r.setUserDob(u.getUserDob());
        r.setAvatar(u.getAvatar());
        if (u.getRole() != null) r.setRole(u.getRole().getName()); // "ADMIN"/"USER"
        return r;
    }

    @Override
    @Transactional
    public UserResponse promoteToAdmin(Integer userId) {
        UserEntity u = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        u.setRole(roleOrThrow("ADMIN"));
        userRepository.save(u);
        return toResponse(u);
    }

    @Override
    @Transactional
    public UserResponse demoteToUser(Integer userId) {
        UserEntity u = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        u.setRole(roleOrThrow("USER"));
        userRepository.save(u);
        return toResponse(u);
    }

    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::toResponse);
    }

    @Override
    public Page<UserResponse> getUsersByStatus(String status, Pageable pageable) {
        return userRepository.findByStatus(status, pageable).map(this::toResponse);
    }
}
