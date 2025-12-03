package com.javaweb.service;

import com.javaweb.model.request.MediaCreateRequest;
import com.javaweb.model.request.MediaSearchRequest;
import com.javaweb.model.response.MediaSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MediaService {
    Page<MediaSearchResponse> getMedias(Pageable pageable, MediaSearchRequest mediaSearchRequest);
    MediaSearchResponse getMediasDetail(Integer mediaItemId);
    MediaSearchResponse uploadMediaItem(MediaCreateRequest mediaCreateRequest, Integer userId);
    void deleteMediaItem(Integer mediaItemId, Integer userId);
    MediaSearchResponse updateMediaItem(Integer mediaItemId, MediaCreateRequest mediaCreateRequest, Integer userId);
}
