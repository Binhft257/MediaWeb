package com.javaweb.service;

import com.javaweb.model.response.MediaUploadHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MediaHistoryService {
    Page<MediaUploadHistoryResponse> getMyUploadHistory(Integer userId, Pageable pageable);
}
