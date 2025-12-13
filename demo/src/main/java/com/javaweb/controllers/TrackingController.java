package com.javaweb.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaweb.model.request.TrackMediaRequest;
import com.javaweb.model.response.MediaSearchResponse;
import com.javaweb.security.SecurityUtils;
import com.javaweb.service.TrackingService;

@RestController
@RequestMapping("/api/users/me/tracking")
public class TrackingController {

    @Autowired
    private TrackingService trackingService;

    // List all media items tracked by the user
    @GetMapping
    public ResponseEntity<List<MediaSearchResponse>> getTrackedMediaItems() {
        Integer userId = SecurityUtils.getPrincipal().getId();
        List<MediaSearchResponse> response = trackingService.getTrackedMediaItems(userId);
        return ResponseEntity.ok(response);
    }

    // Log a media item
    @PostMapping
    public ResponseEntity<MediaSearchResponse> trackMediaItem(@RequestBody TrackMediaRequest request) {
        Integer userId = SecurityUtils.getPrincipal().getId();
        MediaSearchResponse response = trackingService.trackMediaItem(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response); 
    }

    // Delete a media item
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMediaItemTracked(@PathVariable Integer id) {
        Integer userId = SecurityUtils.getPrincipal().getId();
        trackingService.deleteMediaItemTracked(id, userId);
        return ResponseEntity.noContent().build();
    }
}
