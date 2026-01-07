package com.javaweb.service;

import com.javaweb.model.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminUserService {

    UserResponse promoteToAdmin(Integer userId);

    UserResponse demoteToUser(Integer userId);

    Page<UserResponse> getAllUsers(Pageable pageable);

    Page<UserResponse> getUsersByStatus(String status, Pageable pageable);
}
