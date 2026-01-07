package com.javaweb.repository;

import com.javaweb.entity.MediaItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaItemRepository extends JpaRepository<MediaItemEntity,Integer> {
    Page<MediaItemEntity> findByUploadedByOrderByCreatedAtDesc(Integer uploadedBy, Pageable pageable);
}
