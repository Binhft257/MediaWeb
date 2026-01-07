package com.javaweb.service;

import java.util.List;

import com.javaweb.model.request.TrackMediaRequest;
import com.javaweb.model.request.UpdateTrackingRequest;
import com.javaweb.model.response.TrackingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TrackingService {
    Page<TrackingResponse> getTrackedMediaItems(Integer userId, String type, Pageable pageable);
    TrackingResponse trackMediaItem(TrackMediaRequest request, Integer userId);
    void deleteMediaItemTracked(Integer userMediaLogId, Integer userId);
    TrackingResponse updateTrackedMediaItem(Integer logId, UpdateTrackingRequest request, Integer userId);
}
