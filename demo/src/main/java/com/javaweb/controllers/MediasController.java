package com.javaweb.controllers;

import com.javaweb.model.request.MediaCreateRequest;
import com.javaweb.model.request.MediaSearchRequest;
import com.javaweb.model.response.MediaSearchResponse;
import com.javaweb.security.SecurityUtils;
import com.javaweb.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medias")
public class MediasController {

    @Autowired
    private MediaService mediaService;

    // Search Feature with queries and filters
    @GetMapping
    public Page<MediaSearchResponse> searchMedias(
            MediaSearchRequest request,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        PageRequest pageable = PageRequest.of(page - 1, limit);
        return mediaService.getMedias(pageable, request);
    }

    // View a media item details
    @GetMapping("/{id}")
    public ResponseEntity<MediaSearchResponse> getDetail(@PathVariable Integer id) {
        MediaSearchResponse response = mediaService.getMediasDetail(id);
        return ResponseEntity.ok(response);
    }

    // Upload a media item
    @PostMapping
    public ResponseEntity<MediaSearchResponse> uploadMediaItem(@RequestBody MediaCreateRequest request) {
        Integer userId = SecurityUtils.getPrincipal().getId();
        MediaSearchResponse response = mediaService.uploadMediaItem(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response); 
    }

    // Delete a media item
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMediaItem(@PathVariable int id) {
        Integer userId = SecurityUtils.getPrincipal().getId();
        mediaService.deleteMediaItem(id, userId);
        return ResponseEntity.noContent().build();
    }

    // Edit a media item
    @PutMapping("/{id}")
    public ResponseEntity<MediaSearchResponse> updateMediaItem(@PathVariable Integer id, @RequestBody MediaCreateRequest request) {
        Integer userId = SecurityUtils.getPrincipal().getId();
        MediaSearchResponse updatedMedia = mediaService.updateMediaItem(id, request, userId);
        return ResponseEntity.ok(updatedMedia);
    }
}
