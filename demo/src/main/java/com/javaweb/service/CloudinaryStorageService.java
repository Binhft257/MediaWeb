package com.javaweb.service;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Set;

@Service
public class CloudinaryStorageService {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    private static final long MAX_SIZE = 5L * 1024 * 1024; // 5MB

    @Autowired
    private Cloudinary cloudinary;

    // =========================
    // COMMON VALIDATION
    // =========================
    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new RuntimeException("Only JPG / PNG / WebP images are allowed");
        }

        if (file.getSize() > MAX_SIZE) {
            throw new RuntimeException("Max file size is 5MB");
        }
    }

    // =========================
    // UPLOAD AVATAR
    // =========================
    public String uploadAvatar(MultipartFile file, Integer userId) {
        validateImage(file);
        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    Map.of(
                            "folder", "avatars",
                            "public_id", "user_" + userId,
                            "overwrite", true
                    )
            );
            return result.get("secure_url").toString();
        } catch (Exception e) {
            throw new RuntimeException("Upload avatar failed", e);
        }
    }

    // =========================
    // UPLOAD MEDIA IMAGE
    // =========================
    public String uploadMediaImage(MultipartFile file, Integer mediaItemId) {
        validateImage(file);
        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    Map.of(
                            "folder", "media",
                            "public_id", "media_" + mediaItemId,
                            "overwrite", true
                    )
            );
            return result.get("secure_url").toString();
        } catch (Exception e) {
            throw new RuntimeException("Upload media image failed", e);
        }
    }

    // =========================
    // DELETE IMAGE BY URL
    // =========================
    public void deleteByUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) return;

        try {
            // Example URL:
            // https://res.cloudinary.com/xxx/image/upload/v170000/media/media_12.jpg
            String publicId = imageUrl
                    .substring(imageUrl.indexOf("/upload/") + 8) // media/media_12.jpg
                    .replaceFirst("^v\\d+/", "")                 // media/media_12.jpg
                    .replaceAll("\\.[a-zA-Z0-9]+$", "");         // media/media_12

            cloudinary.uploader().destroy(publicId, Map.of());
        } catch (Exception ignored) {
            // không throw để tránh fail request nếu ảnh đã bị xóa
        }
    }
}
