package com.javaweb.service.impl;

import com.javaweb.entity.MediaItemEntity;
import com.javaweb.entity.UserEntity;
import com.javaweb.entity.UserHistoryEntity;
import com.javaweb.model.response.UserHistoryResponse;
import com.javaweb.repository.MediaItemRepository;
import com.javaweb.repository.UserHistoryRepository;
import com.javaweb.repository.UserRepository;
import com.javaweb.service.UserHistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class UserHistoryServiceImpl implements UserHistoryService {

    private final UserHistoryRepository userHistoryRepository;
    private final UserRepository userRepository;
    private final MediaItemRepository mediaItemRepository;

    public UserHistoryServiceImpl(UserHistoryRepository userHistoryRepository,
                                  UserRepository userRepository,
                                  MediaItemRepository mediaItemRepository) {
        this.userHistoryRepository = userHistoryRepository;
        this.userRepository = userRepository;
        this.mediaItemRepository = mediaItemRepository;
    }

    private UserHistoryResponse toRes(UserHistoryEntity h) {
        UserHistoryResponse r = new UserHistoryResponse();
        MediaItemEntity m = h.getMediaItem();

        r.setMediaItemId(m.getMediaItemId());
        r.setTitle(m.getTitle());
        r.setUrlItem(m.getUrlItem());
        r.setCreatedAt(h.getCreatedAt());
        r.setTypeName(m.getMediaType() == null ? null : m.getMediaType().getTypeName());
        return r;
    }

    @Override
    @Transactional
    public void recordView(Integer userId, Integer mediaItemId) {
        // đảm bảo tồn tại user & media
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        MediaItemEntity media = mediaItemRepository.findById(mediaItemId)
                .orElseThrow(() -> new RuntimeException("Media item not found"));

        Date now = new Date();

        // upsert: có rồi thì update created_at, chưa có thì insert mới
        UserHistoryEntity history = userHistoryRepository
                .findByUser_IdAndMediaItem_MediaItemId(userId, mediaItemId)
                .orElseGet(UserHistoryEntity::new);

        history.setUser(user);
        history.setMediaItem(media);
        history.setCreatedAt(now);

        userHistoryRepository.save(history);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserHistoryResponse> getMyHistory(Integer userId, Pageable pageable) {
        return userHistoryRepository.findByUser_IdOrderByCreatedAtDesc(userId, pageable)
                .map(this::toRes);
    }

    @Override
    @Transactional
    public void deleteOne(Integer userId, Integer mediaItemId) {
        userHistoryRepository.deleteByUser_IdAndMediaItem_MediaItemId(userId, mediaItemId);
    }

    @Override
    @Transactional
    public void clearAll(Integer userId) {
        userHistoryRepository.deleteByUser_Id(userId);
    }
}
