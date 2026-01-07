package com.javaweb.service;

import com.javaweb.model.request.MediaCreateRequest;
import com.javaweb.model.request.MediaSearchRequest;
import com.javaweb.model.request.UserCommentRequest;
import com.javaweb.model.request.UserRatingRequest;
import com.javaweb.model.response.MediaSearchResponse;
import com.javaweb.model.response.UserCommentResponse;
import com.javaweb.model.response.UserRatingResponse;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MediaService {
    Page<MediaSearchResponse> getMedias(Pageable pageable, MediaSearchRequest mediaSearchRequest);
    MediaSearchResponse getMediasDetail(Integer mediaItemId);
    MediaSearchResponse uploadMediaItem(MediaCreateRequest mediaCreateRequest, Integer userId);
    void deleteMediaItem(Integer mediaItemId, Integer userId);
    MediaSearchResponse updateMediaItem(Integer mediaItemId, MediaCreateRequest mediaCreateRequest, Integer userId);
    List<UserCommentResponse> getMediaReviews(Integer mediaItemId);

    UserCommentResponse createMediaReview(Integer mediaItemId, UserCommentRequest request, Integer userId);

    UserCommentResponse updateMediaReview(Integer mediaItemId, Integer reviewId, UserCommentRequest request, Integer userId);

    void deleteMediaReview(Integer mediaItemId, Integer reviewId, Integer userId);
    List<UserRatingResponse> getMediaRatings(Integer mediaItemId);
    UserRatingResponse createMediaRating(Integer mediaItemId, UserRatingRequest request, Integer userId);
    UserRatingResponse updateMediaRating(Integer mediaItemId, UserRatingRequest request, Integer userId);
    void deleteMediaRating(Integer mediaItemId, Integer userId);
}
