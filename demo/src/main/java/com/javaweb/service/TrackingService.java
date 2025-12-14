package com.javaweb.service;

import java.util.List;

import com.javaweb.model.request.TrackMediaRequest;
import com.javaweb.model.response.MediaSearchResponse;

public interface TrackingService {
    List<MediaSearchResponse> getTrackedMediaItems(Integer userId);
    MediaSearchResponse trackMediaItem(TrackMediaRequest request, Integer userId);
    void deleteMediaItemTracked(Integer userMediaLogIdd, Integer userId);
}
