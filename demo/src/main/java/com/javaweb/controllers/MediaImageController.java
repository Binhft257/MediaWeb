package com.javaweb.controllers;

import com.javaweb.model.response.ImagePathResponse;
import com.javaweb.security.SecurityUtils;
import com.javaweb.service.UploadImageService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/medias")
public class MediaImageController {

    private final UploadImageService uploadImageService;

    public MediaImageController(UploadImageService uploadImageService) {
        this.uploadImageService = uploadImageService;
    }

    private String baseUrl(HttpServletRequest req) {
        int port = req.getServerPort();
        boolean defaultPort = (port == 80 || port == 443);
        return req.getScheme() + "://" + req.getServerName() + (defaultPort ? "" : ":" + port);
    }

    @PostMapping("/{mediaItemId}/image")
    public ResponseEntity<ImagePathResponse> uploadOrUpdateMediaImage(
            @PathVariable Integer mediaItemId,
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request
    ) {
        Integer me = SecurityUtils.getPrincipal().getId();
        boolean isAdmin = SecurityUtils.isAdmin();

        String path = uploadImageService.uploadOrUpdateMediaImage(mediaItemId, file, me, isAdmin);
        return ResponseEntity.ok(new ImagePathResponse(path, baseUrl(request) + path));
    }

    // xoá ảnh media (OWNER hoặc ADMIN)
    @DeleteMapping("/{mediaItemId}/image")
    public ResponseEntity<Void> deleteMediaImage(@PathVariable Integer mediaItemId) {
        Integer me = SecurityUtils.getPrincipal().getId();
        boolean isAdmin = SecurityUtils.isAdmin();

        uploadImageService.deleteMediaImage(mediaItemId, me, isAdmin);
        return ResponseEntity.noContent().build();
    }

}
