package com.javaweb.controllers;

import com.javaweb.model.response.ImagePathResponse;
import com.javaweb.security.SecurityUtils;
import com.javaweb.service.UploadImageService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
public class UserAvatarController {

    private final UploadImageService uploadImageService;

    public UserAvatarController(UploadImageService uploadImageService) {
        this.uploadImageService = uploadImageService;
    }

    private String baseUrl(HttpServletRequest req) {
        int port = req.getServerPort();
        boolean defaultPort = (port == 80 || port == 443);
        return req.getScheme() + "://" + req.getServerName() + (defaultPort ? "" : ":" + port);
    }

    @PostMapping("/me/avatar")
    public ResponseEntity<ImagePathResponse> uploadOrUpdateMyAvatar(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request
    ) {
        Integer me = SecurityUtils.getPrincipal().getId();
        String path = uploadImageService.uploadOrUpdateMyAvatar(me, file); // DB lưu PATH
        return ResponseEntity.ok(new ImagePathResponse(path, baseUrl(request) + path));
    }

    @DeleteMapping("/me/avatar")
    public ResponseEntity<Void> deleteMyAvatar() {
        Integer me = SecurityUtils.getPrincipal().getId();
        uploadImageService.deleteMyAvatar(me);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/avatar")
    public ResponseEntity<ImagePathResponse> adminUploadOrUpdateAvatar(
            @PathVariable Integer userId,
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request
    ) {
        boolean isAdmin = SecurityUtils.isAdmin();
        String path = uploadImageService.adminUploadOrUpdateAvatar(userId, file, isAdmin);
        return ResponseEntity.ok(new ImagePathResponse(path, baseUrl(request) + path));
    }

    @DeleteMapping("/{userId}/avatar")
    public ResponseEntity<Void> adminDeleteAvatar(@PathVariable Integer userId) {
        boolean isAdmin = SecurityUtils.isAdmin();
        uploadImageService.adminDeleteAvatar(userId, isAdmin);
        return ResponseEntity.noContent().build();
    }
}
