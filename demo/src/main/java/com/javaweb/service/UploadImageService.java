package com.javaweb.service;

import com.javaweb.entity.MediaItemEntity;
import com.javaweb.entity.UserEntity;
import com.javaweb.repository.MediaItemRepository;
import com.javaweb.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadImageService {

    private final UserRepository userRepository;
    private final MediaItemRepository mediaItemRepository;
    private final CloudinaryStorageService cloudinaryStorageService;

    public UploadImageService(UserRepository userRepository,
                              MediaItemRepository mediaItemRepository,
                              CloudinaryStorageService cloudinaryStorageService) {
        this.userRepository = userRepository;
        this.mediaItemRepository = mediaItemRepository;
        this.cloudinaryStorageService = cloudinaryStorageService;
    }

    // =========================
    // AVATAR (ME)
    // =========================

    /**
     * 1 API cho cả thêm mới / đổi avatar:
     * - xoá ảnh cũ trên Cloudinary nếu có
     * - upload ảnh mới
     * - DB lưu FULL URL (secure_url)
     */
    @Transactional
    public String uploadOrUpdateMyAvatar(Integer userId, MultipartFile file) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // xoá avatar cũ nếu có (đang lưu full URL)
        cloudinaryStorageService.deleteByUrl(user.getAvatar());

        // upload avatar mới -> trả FULL URL
        String newUrl = cloudinaryStorageService.uploadAvatar(file, userId);

        user.setAvatar(newUrl);
        userRepository.save(user);
        return newUrl;
    }

    /**
     * Xoá avatar của chính mình:
     * - xoá ảnh trên Cloudinary nếu có
     * - set avatar = null
     */
    @Transactional
    public void deleteMyAvatar(Integer userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        cloudinaryStorageService.deleteByUrl(user.getAvatar());
        user.setAvatar(null);
        userRepository.save(user);
    }

    // =========================
    // AVATAR (ADMIN MANAGE USER)
    // =========================

    /**
     * Admin đổi avatar cho user khác.
     */
    @Transactional
    public String adminUploadOrUpdateAvatar(Integer targetUserId, MultipartFile file, boolean isAdmin) {
        if (!isAdmin) throw new AccessDeniedException("Admin only");

        UserEntity user = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        cloudinaryStorageService.deleteByUrl(user.getAvatar());
        String newUrl = cloudinaryStorageService.uploadAvatar(file, targetUserId);

        user.setAvatar(newUrl);
        userRepository.save(user);
        return newUrl;
    }

    /**
     * Admin xoá avatar user khác.
     */
    @Transactional
    public void adminDeleteAvatar(Integer targetUserId, boolean isAdmin) {
        if (!isAdmin) throw new AccessDeniedException("Admin only");

        UserEntity user = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        cloudinaryStorageService.deleteByUrl(user.getAvatar());
        user.setAvatar(null);
        userRepository.save(user);
    }

    // =========================
    // MEDIA IMAGE (OWNER or ADMIN)
    // =========================

    private void assertOwnerOrAdmin(MediaItemEntity media, Integer userId, boolean isAdmin) {
        if (isAdmin) return;
        if (media.getUploadedBy() == null || !media.getUploadedBy().equals(userId)) {
            throw new AccessDeniedException("You are not allowed");
        }
    }

    /**
     * 1 API cho cả thêm mới / đổi ảnh media:
     * - chỉ OWNER hoặc ADMIN
     * - xoá ảnh cũ trên Cloudinary nếu có
     * - upload ảnh mới
     * - DB lưu FULL URL vào url_item
     */
    @Transactional
    public String uploadOrUpdateMediaImage(Integer mediaItemId, MultipartFile file, Integer userId, boolean isAdmin) {
        MediaItemEntity media = mediaItemRepository.findById(mediaItemId)
                .orElseThrow(() -> new RuntimeException("Media item not found"));

        assertOwnerOrAdmin(media, userId, isAdmin);

        // xoá ảnh cũ (đang lưu full URL trong urlItem)
        cloudinaryStorageService.deleteByUrl(media.getUrlItem());

        // upload ảnh mới -> trả FULL URL
        String newUrl = cloudinaryStorageService.uploadMediaImage(file, mediaItemId);

        // dùng url_item để lưu FULL URL ảnh
        media.setUrlItem(newUrl);
        mediaItemRepository.save(media);
        return newUrl;
    }

    /**
     * Xoá ảnh media (OWNER hoặc ADMIN):
     * - xoá ảnh trên Cloudinary nếu có
     * - set url_item = null
     */
    @Transactional
    public void deleteMediaImage(Integer mediaItemId, Integer userId, boolean isAdmin) {
        MediaItemEntity media = mediaItemRepository.findById(mediaItemId)
                .orElseThrow(() -> new RuntimeException("Media item not found"));

        assertOwnerOrAdmin(media, userId, isAdmin);

        cloudinaryStorageService.deleteByUrl(media.getUrlItem());
        media.setUrlItem(null);
        mediaItemRepository.save(media);
    }
}
