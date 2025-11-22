package com.javaweb.repository;

import com.javaweb.entity.MediaItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaItemRepository extends JpaRepository<MediaItemEntity,Long> {
}
