package com.javaweb.controllers;

import com.javaweb.model.response.MediaUploadHistoryResponse;
import com.javaweb.security.SecurityUtils;
import com.javaweb.service.MediaHistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/me")
public class MediaHistoryController {

    private final MediaHistoryService mediaHistoryService;

    public MediaHistoryController(MediaHistoryService mediaHistoryService) {
        this.mediaHistoryService = mediaHistoryService;
    }

    // GET /api/users/me/media-uploads?page=0&size=10&sort=createdAt,desc
    @GetMapping("/media-uploads")
    public ResponseEntity<Page<MediaUploadHistoryResponse>> myUploads(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Integer me = SecurityUtils.getPrincipal().getId();
        return ResponseEntity.ok(mediaHistoryService.getMyUploadHistory(me, pageable));
    }
}
