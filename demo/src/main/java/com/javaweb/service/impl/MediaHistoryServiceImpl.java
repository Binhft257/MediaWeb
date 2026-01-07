package com.javaweb.service.impl;

import com.javaweb.entity.MediaItemEntity;
import com.javaweb.model.response.MediaUploadHistoryResponse;
import com.javaweb.repository.MediaItemRepository;
import com.javaweb.service.MediaHistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MediaHistoryServiceImpl implements MediaHistoryService {

    private final MediaItemRepository mediaItemRepository;

    public MediaHistoryServiceImpl(MediaItemRepository mediaItemRepository) {
        this.mediaItemRepository = mediaItemRepository;
    }

    private MediaUploadHistoryResponse toRes(MediaItemEntity e) {
        MediaUploadHistoryResponse r = new MediaUploadHistoryResponse();
        r.setMediaItemId(e.getMediaItemId());
        r.setTitle(e.getTitle());
        r.setCreatedAt(e.getCreatedAt());
        r.setImagePath(e.getUrlItem()); // DB lưu PATH (/uploads/media/...)
        r.setDescription(e.getDescription());
        r.setMediaType(e.getMediaType() == null ? null : e.getMediaType().getTypeName());
        return r;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MediaUploadHistoryResponse> getMyUploadHistory(Integer userId, Pageable pageable) {
        return mediaItemRepository.findByUploadedByOrderByCreatedAtDesc(userId, pageable)
                .map(this::toRes);
    }

}
