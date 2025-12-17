package com.javaweb.controllers;

import com.javaweb.model.request.MediaCreateRequest;
import com.javaweb.model.request.MediaSearchRequest;
import com.javaweb.model.request.UserCommentRequest;
import com.javaweb.model.request.UserRatingRequest;
import com.javaweb.model.response.MediaSearchResponse;
import com.javaweb.model.response.UserCommentResponse;
import com.javaweb.model.response.UserRatingResponse;
import com.javaweb.security.SecurityUtils;
import com.javaweb.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<Void> deleteMediaItem(@PathVariable Integer id) {
       Integer userId = SecurityUtils.getPrincipal().getId();
        mediaService.deleteMediaItem(id, userId);
        return ResponseEntity.noContent().build();
    }

    // Edit a media item
    @PutMapping("/{id}")
    public ResponseEntity<MediaSearchResponse> updateMediaItem(@PathVariable Integer id, @RequestBody MediaCreateRequest request) {
        Integer userId = SecurityUtils.getPrincipal().getId();
        MediaSearchResponse reponse = mediaService.updateMediaItem(id, request, userId);
        return ResponseEntity.ok(reponse);
    }

    // List all reviews on a media item
    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<UserCommentResponse>> getReviews(@PathVariable Integer id) {
        List<UserCommentResponse> response = mediaService.getMediaReviews(id);
        return ResponseEntity.ok(response);
    }

    // Create the personal review on a media item
    @PostMapping("/{id}/reviews")
    public ResponseEntity<UserCommentResponse> createReview(@PathVariable Integer id, @RequestBody UserCommentRequest request) {
       Integer userId = SecurityUtils.getPrincipal().getId();
        UserCommentResponse response = mediaService.createMediaReview(id, request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response); 
    }

    // Edit the personal review on a media item
    @PutMapping("/{mediaItemId}/reviews")
    public ResponseEntity<UserCommentResponse> updateReview(@PathVariable Integer mediaItemId, @RequestBody UserCommentRequest request) {
        Integer userId = SecurityUtils.getPrincipal().getId();
        UserCommentResponse reponse = mediaService.updateMediaReview(mediaItemId, request, userId);
        return ResponseEntity.ok(reponse);
    }

    // Delete the personal review on a media item
    @DeleteMapping("/{mediaItemId}/reviews")
    public ResponseEntity<Void> deleteReview(@PathVariable Integer mediaItemId) {
       Integer userId = SecurityUtils.getPrincipal().getId();
        mediaService.deleteMediaReview(mediaItemId, userId);
        return ResponseEntity.noContent().build();
    }

    // List all ratings on a media item
    @GetMapping("/{id}/ratings")
    public ResponseEntity<List<UserRatingResponse>> getRatings(@PathVariable Integer id) {
        List<UserRatingResponse> response = mediaService.getMediaRatings(id);
        return ResponseEntity.ok(response);
    }

    // Create the personal rating on a media item
    @PostMapping("/{id}/ratings")
    public ResponseEntity<UserRatingResponse> createRating(@PathVariable Integer id, @RequestBody UserRatingRequest request) {
        Integer userId = SecurityUtils.getPrincipal().getId();
        UserRatingResponse response = mediaService.createMediaRating(id, request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response); 
    }

    // Edit the personal rating on a media item
    @PutMapping("/{mediaItemId}/ratings")
    public ResponseEntity<UserRatingResponse> updateRating(@PathVariable Integer mediaItemId, @RequestBody UserRatingRequest request) {
        Integer userId = SecurityUtils.getPrincipal().getId();
        UserRatingResponse reponse = mediaService.updateMediaRating(mediaItemId, request, userId);
        return ResponseEntity.ok(reponse);
    }

    // Delete the personal rating on a media item
    @DeleteMapping("/{mediaItemId}/ratings")
    public ResponseEntity<Void> deleteRating(@PathVariable Integer mediaItemId) {
        Integer userId = SecurityUtils.getPrincipal().getId();
        mediaService.deleteMediaRating(mediaItemId, userId);
        return ResponseEntity.noContent().build();
    }
}
