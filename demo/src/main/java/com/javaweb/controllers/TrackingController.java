package com.javaweb.controllers;

import java.util.List;

import com.javaweb.model.request.UpdateTrackingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.javaweb.model.request.TrackMediaRequest;
import com.javaweb.model.response.TrackingResponse;
import com.javaweb.security.SecurityUtils;
import com.javaweb.service.TrackingService;

@RestController
@RequestMapping("/api/users/me/tracking")
public class TrackingController {

    @Autowired
    private TrackingService trackingService;

    @GetMapping
    public ResponseEntity<Page<TrackingResponse>> getTrackedMediaItems(
            @RequestParam(required = false) String type,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Integer userId = SecurityUtils.getPrincipal().getId();
        Page<TrackingResponse> response = trackingService.getTrackedMediaItems(userId, type, pageable);
        return ResponseEntity.ok(response);
    }
    @PostMapping
    public ResponseEntity<TrackingResponse> trackMediaItem(@RequestBody TrackMediaRequest request) {
        Integer userId = SecurityUtils.getPrincipal().getId();
        TrackingResponse response = trackingService.trackMediaItem(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{logId}")
    public ResponseEntity<TrackingResponse> updateTrackedMediaItem(
            @PathVariable Integer logId,
            @RequestBody UpdateTrackingRequest request
    ) {
        Integer userId = SecurityUtils.getPrincipal().getId();
        TrackingResponse response = trackingService.updateTrackedMediaItem(logId, request, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMediaItemTracked(@PathVariable Integer id) {
        Integer userId = SecurityUtils.getPrincipal().getId();
        trackingService.deleteMediaItemTracked(id, userId);
        return ResponseEntity.noContent().build();
    }
}
