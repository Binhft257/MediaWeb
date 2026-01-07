package com.javaweb.controllers;

import com.javaweb.model.response.UserResponse;
import com.javaweb.service.AdminUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getUsers(
            @RequestParam(value = "status", required = false) String status,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        if (status == null || status.isBlank()) {
            return ResponseEntity.ok(adminUserService.getAllUsers(pageable));
        }
        return ResponseEntity.ok(adminUserService.getUsersByStatus(status, pageable));
    }

    // Promote user
    @PutMapping("/{userId}/promote")
    public ResponseEntity<UserResponse> promote(@PathVariable Integer userId) {
        return ResponseEntity.ok(adminUserService.promoteToAdmin(userId));
    }

    // Demote admin
    @PutMapping("/{userId}/demote")
    public ResponseEntity<UserResponse> demote(@PathVariable Integer userId) {
        return ResponseEntity.ok(adminUserService.demoteToUser(userId));
    }
}
